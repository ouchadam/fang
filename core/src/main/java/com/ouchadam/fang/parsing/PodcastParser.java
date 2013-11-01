package com.ouchadam.fang.parsing;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;

import java.io.InputStream;

public class PodcastParser implements XmlParser<Channel> {

    private final InstigatorResult<Channel> instigator;

    public static PodcastParser newInstance(ElementFinder<Channel> channelFinder) {
        return new PodcastParser(new PodcastIntigator(channelFinder, new ParseFinishWatcher() {
            @Override
            public void onFinish() {
                // unneeded
            }
        }));
    }

    PodcastParser(InstigatorResult<Channel> instigator) {
        this.instigator = instigator;
    }

    @Override
    public void parse(InputStream inputStream) {
        SimpleEasyXmlParser.parse(inputStream, instigator);
    }

    @Override
    public Channel getResult() {
        return instigator.getResult();
    }

}

