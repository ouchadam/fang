package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sax.RootElement;
import com.novoda.sexp.RootTag;
import com.novoda.sexp.SimpleTagInstigator;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.novoda.sexp.parser.ParseWatcher;
import com.ouchadam.fang.parsing.InstigatorResult;

import java.util.ArrayList;
import java.util.List;

class TopPodcastInstigator extends SimpleTagInstigator implements InstigatorResult<TopPodcastFeed> {

    private static final String TAG_ROOT = "feed";
    private static final String ROOT_NAMESPACE = "http://www.w3.org/2005/Atom";
    private static final String TAG_ENTRY = "entry";
    private final ElementFinder<Entry> feedFinder;
    private final TopPodcastFeedParser topPodcastFeedParser;

    public TopPodcastInstigator(ElementFinder<Entry> feedFinder, TopPodcastFeedParser topPodcastFeedParser, ParseFinishWatcher parseFinishWatcher) {
        super(feedFinder, TAG_ENTRY, parseFinishWatcher);
        this.feedFinder = feedFinder;
        this.topPodcastFeedParser = topPodcastFeedParser;
    }

    @Override
    public RootTag getRootTag() {
        return RootTag.create(TAG_ROOT, ROOT_NAMESPACE);
    }

    @Override
    public void create(RootElement element) {
        feedFinder.find(element, ROOT_NAMESPACE, TAG_ENTRY);
    }

    @Override
    public TopPodcastFeed getResult() {
        return topPodcastFeedParser.get();
    }
}
