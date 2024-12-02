package org.ehb.wout.torsync.scraper;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.util.Scanner;

public class YTSScraper {

    public static void main(String[] args) {
        System.setProperty("webdriver.gecko.driver", "C:\\Users\\depeu\\Downloads\\geckodriver.exe");

        FirefoxOptions options = new FirefoxOptions();
        options.setBinary("C:\\Program Files\\Mozilla Firefox\\firefox.exe");

        FirefoxDriver driver = new FirefoxDriver(options);

        Scanner scanner = new Scanner(System.in);

        try {
            System.out.print("Enter the movie title: ");
            String movieTitle = scanner.nextLine().trim();

            String formattedTitle = movieTitle.replaceAll(" ", "-").toLowerCase();


            String movieUrl = "https://yts.mx/movies/" + formattedTitle;


            driver.get(movieUrl);


            WebElement magnetLinkElement = driver.findElement(By.cssSelector("a[href^='magnet:']"));

            if (magnetLinkElement != null) {
                String magnetLink = magnetLinkElement.getAttribute("href");
                System.out.println("Magnet Link: " + magnetLink);
            } else {
                System.out.println("Magnet link not found for the movie: " + movieTitle);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
            scanner.close();
        }
    }
}
