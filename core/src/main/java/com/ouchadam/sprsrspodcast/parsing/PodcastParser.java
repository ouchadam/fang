package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.sprsrspodcast.parsing.domain.channel.Channel;

import java.io.InputStream;

public class PodcastParser {

    private final InstigatorResult<Channel> instigator;

    public static PodcastParser newInstance(ParseFinishWatcher parseFinishWatcher) {
        ElementFinderFactory finderFactory = SimpleEasyXmlParser.getElementFinderFactory();
        ElementFinder<Channel> channelFinder = finderFactory.getTypeFinder(new ChannelParser(finderFactory));
        return new PodcastParser(new PodcastIntigator(channelFinder, parseFinishWatcher));
    }

    PodcastParser(InstigatorResult<Channel> instigator) {
        this.instigator = instigator;
    }

    public void parse(InputStream inputStream) {
        SimpleEasyXmlParser.parse(inputStream, instigator);
    }

}

