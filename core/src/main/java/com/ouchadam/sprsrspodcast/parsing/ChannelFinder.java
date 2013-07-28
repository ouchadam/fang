package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.ouchadam.sprsrspodcast.domain.channel.Channel;

public class ChannelFinder {

    public static ElementFinder<Channel> newInstance() {
        ElementFinderFactory finderFactory = SimpleEasyXmlParser.getElementFinderFactory();
        return finderFactory.getTypeFinder(new ChannelParser(finderFactory));
    }

}
