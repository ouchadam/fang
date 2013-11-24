package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.notils.java.Collections;
import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;

import org.xml.sax.Attributes;

import java.util.List;

class TopPodcastFeedParser implements ParseWatcher<Entry> {

    private FeedHolder feedHolder;

    TopPodcastFeedParser() {
        feedHolder = new FeedHolder();
    }

    @Override
    public void onParsed(Entry entry) {
        System.out.println("got entry");
        feedHolder.entries.add(entry);
    }

    public TopPodcastFeed get() {
        return feedHolder.asFeed();
    }

    private static class FeedHolder {
        List<Entry> entries = Collections.newArrayList();

        TopPodcastFeed asFeed() {
            return new TopPodcastFeed(entries);
        }
    }

}
