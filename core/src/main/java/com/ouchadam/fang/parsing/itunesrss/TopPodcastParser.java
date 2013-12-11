package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.XMLReaderBuilder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.parsing.InstigatorResult;
import com.ouchadam.fang.parsing.XmlParser;
import com.sun.org.apache.regexp.internal.recompile;

import org.xml.sax.XMLReader;

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
        try {
            SimpleEasyXmlParser.parse(inputStream, instigator, getXmlReader());
        } catch (XMLReaderBuilder.XMLReaderCreationException e) {
            throw new RuntimeException("Couldn't create xml reader");
        }
    }

    private XMLReader getXmlReader() throws XMLReaderBuilder.XMLReaderCreationException {
        return new XMLReaderBuilder()
                .allowNamespaceProcessing(true)
                .build();
    }

    @Override
    public TopPodcastFeed getResult() {
        return instigator.getResult();
    }

}
