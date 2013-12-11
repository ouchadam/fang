package com.ouchadam.fang.parsing.itunesrss;

import java.util.List;

public class TopPodcastFeed {

    private final List<Entry> entries;

    public interface ForEach {
        void onEach(Entry entry);
    }

    public TopPodcastFeed(List<Entry> entries) {
        this.entries = entries;
    }

    public List<Entry> getEntries() {
        return entries;
    }

    public void forEach(ForEach forEach) {
        for (Entry entry : entries) {
            forEach.onEach(entry);
        }
    }
}
