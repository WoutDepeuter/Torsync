package org.ehb.wout.torsync.dao;

import org.ehb.wout.torsync.dto.MovieSearchResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.ResponseEntity;

@Service
public class TMDbApiClient {

    @Value("${tmdb.api.key}")
    private String apiKey;

    private final String BASE_URL = "https://api.themoviedb.org/3";
    private final RestTemplate restTemplate;

    public TMDbApiClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }


    public MovieSearchResponse searchMovies(String query, int page) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL + "/search/movie")
                .queryParam("api_key", apiKey)
                .queryParam("query", query)
                .queryParam("page", page)
                .toUriString();


        ResponseEntity<MovieSearchResponse> response = restTemplate.getForEntity(url, MovieSearchResponse.class);

        return response.getBody();
}}

