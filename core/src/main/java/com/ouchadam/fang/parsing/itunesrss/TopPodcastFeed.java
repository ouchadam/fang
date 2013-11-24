package com.ouchadam.fang.parsing.itunesrss;

import java.util.List;

public class TopPodcastFeed {

    private final List<Entry> entries;

    public TopPodcastFeed(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        return entries;
    }
}
