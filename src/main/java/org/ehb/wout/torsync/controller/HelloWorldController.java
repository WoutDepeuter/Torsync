package org.ehb.wout.torsync.controller;

import org.ehb.wout.torsync.QbittorrentDownloader;
import org.ehb.wout.torsync.dto.MovieSearchResponse;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HelloWorldController {

    @GetMapping("/")
    public String index() {
        return "search";
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
                model.addAttribute("magnetLink", magnetLink);
            } else {
                model.addAttribute("magnetLink", "Magnet link not found for the movie: " + movieTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("magnetLink", "Error occurred while fetching the magnet link.");
        } finally {
            driver.quit();
        }

        return "result";
    }
    private final QbittorrentDownloader downloader = new QbittorrentDownloader();

    @PostMapping("/download")
    public String downloadMagnet(@RequestParam("magnetLink") String magnetLink) {
        downloader.downloadMagnetLink(magnetLink);
        System.out.println(magnetLink);
        return "redirect:/";
    }
}






