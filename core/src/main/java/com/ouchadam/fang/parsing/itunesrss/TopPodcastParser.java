package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.parsing.InstigatorResult;
import com.ouchadam.fang.parsing.XmlParser;

import java.io.InputStream;

public class TopPodcastParser implements XmlParser<TopPodcastFeed> {

    private final InstigatorResult<TopPodcastFeed> instigator;

    public static TopPodcastParser newInstance() {
        TopPodcastFeedParser topPodcastFeedParser = new TopPodcastFeedParser();
        return new TopPodcastParser(new TopPodcastInstigator(TopPodcastFeedFinder.newInstance(topPodcastFeedParser), topPodcastFeedParser, new ParseFinishWatcher() {
            @Override
            public void onFinish() {
                // unneeded
            }
        }));
    }

    TopPodcastParser(InstigatorResult<TopPodcastFeed> instigator) {
        this.instigator = instigator;
    }

    @Override
    public void parse(InputStream inputStream) {
        SimpleEasyXmlParser.parse(inputStream, instigator);
    }

    @Override
    public TopPodcastFeed getResult() {
        return instigator.getResult();
    }

}
