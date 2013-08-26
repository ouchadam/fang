package com.ouchadam.fang.api.search;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Result {

    private final String channelOwner;
    private final String channelTitle;
    private final String feedUrl;
    private final String imageUrl;
    private final String mainGenre;
    private final int itemCount;

    public Result(String channelOwner, String channelTitle, String feedUrl, String imageUrl, String mainGenre, int itemCount) {
        this.channelOwner = channelOwner;
        this.channelTitle = channelTitle;
        this.feedUrl = feedUrl;
        this.imageUrl = imageUrl;
        this.mainGenre = mainGenre;
        this.itemCount = itemCount;
    }

    @JsonCreator
    public static Result from(
            @JsonProperty("artistName") String channelOwner,
            @JsonProperty("trackName") String channelTitle,
            @JsonProperty("feedUrl") String feedUrl,
            @JsonProperty("artworkUrl600") String imageUrl,
            @JsonProperty("primaryGenreName") String mainGenre,
            @JsonProperty("trackCount") int itemCount) {
        return new Result(channelOwner, channelTitle, feedUrl, imageUrl, mainGenre, itemCount);
    }

    public String getChannelOwner() {
        return channelOwner;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public String getGenre() {
        return mainGenre;
    }

    public String getFeedUrl() {
        return feedUrl;
    }
}
