package com.ouchadam.fang.api.model;

import com.google.api.client.util.Key;

import java.util.List;

import static com.google.api.client.util.Data.nullOf;

public class FeedList {

    @Key
    private List<Feed> feeds;

    static {
        nullOf(Feed.class);
    }

    public List<Feed> getFeeds() {
        return feeds;
    }

    public FeedList setFeeds(List<Feed> feeds) {
        this.feeds = feeds;
        return this;
    }

}
