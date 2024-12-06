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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        return "movieSearch";
    }

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
                String image = driver.findElement(By.cssSelector("img.img-responsive")).getAttribute("src");
                movieTitle = driver.findElement(By.cssSelector("h1")).getText();

                model.addAttribute("magnetLink", magnetLink);
                model.addAttribute("image", image);
                model.addAttribute("movieTitle", movieTitle);
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



    @GetMapping("/browse/page/{pageNumber}")
    public String browseMovies(@PathVariable("pageNumber") int pageNumber, Model model) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\depeu\\Downloads\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        FirefoxDriver driver = new FirefoxDriver(options);

        try {
            String browseUrl = "https://yts.mx/browse-movies/0/all/all/0/latest/0/en?page=" + pageNumber;
            driver.get(browseUrl);

            List<WebElement> movieElements = driver.findElements(By.cssSelector(".browse-movie-wrap"));

            List<Map<String, String>> movies = new ArrayList<>();

            for (WebElement movieElement : movieElements) {
                WebElement titleElement = movieElement.findElement(By.cssSelector(".browse-movie-title"));
                String movieTitle = titleElement.getText();

                WebElement yearElement = movieElement.findElement(By.cssSelector(".browse-movie-year"));
                String releaseYear = yearElement.getText();

                WebElement genreElement = movieElement.findElement(By.cssSelector("h4"));
                String genre = genreElement.getText();

                WebElement imageElement = movieElement.findElement(By.cssSelector("img"));
                String imageUrl = imageElement.getAttribute("src");

                Map<String, String> movieData = new HashMap<>();
                movieData.put("title", movieTitle);
                movieData.put("image", imageUrl);
                movieData.put("year", releaseYear);
                movieData.put("genre", genre);
                movies.add(movieData);
            }

            model.addAttribute("movies", movies);
            model.addAttribute("currentPage", pageNumber);

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "An error occurred while fetching the movie details.");
        } finally {
            driver.quit();
        }

        return "browseResults";
    }

    @GetMapping("/movie-details/{titleYear}")
    public String getMovieDetails(@PathVariable("titleYear") String titleYear, Model model) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\depeu\\Downloads\\geckodriver.exe");
        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        FirefoxDriver driver = new FirefoxDriver(options);

        try {
            String movieUrl = "https://yts.mx/movies/" + titleYear;
            driver.get(movieUrl);

            WebElement magnetLinkElement = driver.findElement(By.cssSelector("a[href^='magnet:']"));

            if (magnetLinkElement != null) {
                String magnetLink = magnetLinkElement.getAttribute("href");
                String image = driver.findElement(By.cssSelector("img.img-responsive")).getAttribute("src");
                String movieTitle = driver.findElement(By.cssSelector("h1")).getText();

                model.addAttribute("magnetLink", magnetLink);
                model.addAttribute("image", image);
                model.addAttribute("movieTitle", movieTitle);
            } else {
                model.addAttribute("error", "Magnet link not found for the movie: " + titleYear);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Error occurred while fetching the magnet link.");
        } finally {
            driver.quit();
        }

        return "result";
    }
}