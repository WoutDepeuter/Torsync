package org.ehb.wout.torsync.controller;

import org.ehb.wout.torsync.dao.TMDbApiClient;

import org.ehb.wout.torsync.dto.MovieSearchResponse;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MovieController {

    private final TMDbApiClient tmDbApiClient;

    public MovieController(TMDbApiClient tmDbApiClient) {
        this.tmDbApiClient = tmDbApiClient;
    }

    @GetMapping("/search")
    public String searchMovies(@RequestParam(name = "query", defaultValue = "La La Land") String query, Model model) {
        MovieSearchResponse movieSearchResponse = tmDbApiClient.searchMovies(query, 1);

        model.addAttribute("movies", movieSearchResponse.getResults());

        return "movieSearch";
    }
}
