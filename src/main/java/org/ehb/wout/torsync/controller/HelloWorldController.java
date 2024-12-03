package org.ehb.wout.torsync.controller;

import org.ehb.wout.torsync.QbittorrentDownloader;
import org.ehb.wout.torsync.dao.TMDbApiClient;
import org.ehb.wout.torsync.dto.MovieSearchResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
class MovieController {

    private final TMDbApiClient tmDbApiClient;
    private final QbittorrentDownloader downloader = new QbittorrentDownloader();

    public MovieController(TMDbApiClient tmDbApiClient) {
        this.tmDbApiClient = tmDbApiClient;
    }

    @GetMapping("/")
    public String index() {
        return "search";
    }


    @GetMapping("/movies")
    public String showPopularMovies(Model model) {
        MovieSearchResponse popularMovies = tmDbApiClient.fetchPopularMovies(1);
        model.addAttribute("movies", popularMovies.getResults());
        return "movieSearch";
    }
    @GetMapping("/search-tmdb")
    public String searchMoviesOnTMDb(@RequestParam("query") String query, Model model) {
        MovieSearchResponse searchResults = tmDbApiClient.searchMovies(query, 1); // Fetch movies matching the query
        model.addAttribute("movies", searchResults.getResults());
        return "movieSearch"; // Return the view
    }


    // Selenium Search for Torrent
    @GetMapping("/search")
    public String searchMovie(@RequestParam("movieTitle") String movieTitle, Model model) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\depeu\\Downloads\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        FirefoxDriver driver = new FirefoxDriver(options);

        try {
            String formattedTitle = movieTitle.replaceAll(" ", "-").toLowerCase();
            String movieUrl = "https://yts.mx/movies/" + formattedTitle;

            driver.get(movieUrl);

            WebElement magnetLinkElement = driver.findElement(By.cssSelector("a[href^='magnet:']"));

            if (magnetLinkElement != null) {
                String magnetLink = magnetLinkElement.getAttribute("href");
                model.addAttribute("magnetLink", magnetLink);
            } else {
                model.addAttribute("error", "Magnet link not found for the movie: " + movieTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while fetching the magnet link.");
        } finally {
            driver.quit();
        }

        return "result";
    }


    @PostMapping("/download")
    public String downloadMagnet(@RequestParam("magnetLink") String magnetLink) {
        downloader.downloadMagnetLink(magnetLink);
        return "redirect:/";
    }
}
