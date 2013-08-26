package com.ouchadam.fang.api.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.kevinsawicki.http.HttpRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class ItunesSearch {

    private static final String ITUNES_SEARCH_URL = "https://itunes.apple.com/search";

    public void search(String searchTerm) {

        Map<String, String> params = new HashMap<String, String>();

        params.put("term", searchTerm);
        params.put("media", "podcast");
        params.put("limit", "3");

        HttpRequest request = HttpRequest.get(ITUNES_SEARCH_URL, params, true);

        if (request.ok()) {
            InputStream inputStream = request.buffer();
            try {
                SearchResult searchResult = new ObjectMapper().readValue(inputStream, SearchResult.class);
                System.out.println("XXXX : " + searchResult.resultCount);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class SearchResult {

        private final int resultCount;

        public SearchResult(int resultCount) {
            this.resultCount = resultCount;
        }

        @JsonCreator
        public static SearchResult from(@JsonProperty("resultCount") int resultCount) {
            return new SearchResult(resultCount);
        }

    }

}
