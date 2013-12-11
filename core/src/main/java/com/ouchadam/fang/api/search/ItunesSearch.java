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
    private static final String ITUNES_SEARCH_LOOKUP = "https://itunes.apple.com/lookup";

    public SearchResult search(String searchTerm) throws ItunesSearchException {
        searchTerm = searchTerm.replace(" ", "+");
        Map<String, String> params = new HashMap<String, String>();

        params.put("term", searchTerm);
        params.put("media", "podcast");
//        params.put("limit", "3");

        HttpRequest request = HttpRequest.get(ITUNES_SEARCH_URL, params, true);
        SearchResult searchResult = getSearchResult(request);
        if (searchResult != null) {
            return searchResult;
        }
        throw new ItunesSearchException("Search failed");
    }

    public SearchResult lookup(String id) throws ItunesSearchException {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);

        HttpRequest request = HttpRequest.get(ITUNES_SEARCH_LOOKUP, params, true);
        SearchResult searchResult = getSearchResult(request);
        if (searchResult != null) {
            return searchResult;
        }
        throw new ItunesSearchException("Search failed");
    }


    private SearchResult getSearchResult(HttpRequest request) {
        if (request.ok()) {
            InputStream inputStream = request.buffer();
            try {
                return new ObjectMapper().readValue(inputStream, SearchResult.class);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static class ItunesSearchException extends Exception {
        public ItunesSearchException(String message) {
            super(message);
        }
    }

}
