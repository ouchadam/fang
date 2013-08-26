package com.ouchadam.fang.api.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResult {

    private final int resultCount;
    private final List<Result> results;

    public SearchResult(int resultCount, List<Result> results) {
        this.resultCount = resultCount;
        this.results = results;
    }

    @JsonCreator
    public static SearchResult from(@JsonProperty("resultCount") int resultCount,
                                    @JsonProperty("results") List<Result> results) {
        return new SearchResult(resultCount, results);
    }

    public List<Result> getResults() {
        return results;
    }

}
