package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.parsing.InstigatorResult;
import com.ouchadam.fang.parsing.XmlParser;

import java.io.InputStream;

public class TopPodcastParser implements XmlParser<TopPodcastFeed> {

    private final InstigatorResult<TopPodcastFeed> instigator;

//    public static TopPodcastParser newInstance(ElementFinder<TopPodcastFeed> feedFinder) {
//        return new TopPodcastParser(new TopPodcastInstigator(feedFinder, new ParseFinishWatcher() {
//            @Override
//            public void onFinish() {
//                // unneeded
//            }
//        }));
//    }

    TopPodcastParser(InstigatorResult<TopPodcastFeed> instigator) {
        this.instigator = instigator;
    }

    @Override
    public void parse(InputStream inputStream) {
        SimpleEasyXmlParser.parse(inputStream, instigator);
    }

    @Override
    public TopPodcastFeed getResult() {
        System.out.println("asking for result");
        return instigator.getResult();
    }

}
