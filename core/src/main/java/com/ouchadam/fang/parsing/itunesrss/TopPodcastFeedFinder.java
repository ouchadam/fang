package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;

public class TopPodcastFeedFinder {

    public static ElementFinder<Entry> newInstance(ParseWatcher<Entry> parseWatcher) {
        ElementFinderFactory finderFactory = SimpleEasyXmlParser.getElementFinderFactory();
        return finderFactory.getListElementFinder(new EntryParser(finderFactory), parseWatcher);
    }

}
