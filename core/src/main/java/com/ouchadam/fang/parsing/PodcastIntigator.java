package com.ouchadam.fang.parsing;

import com.novoda.sexp.SimpleTagInstigator;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;

class PodcastIntigator extends SimpleTagInstigator implements InstigatorResult<Channel> {

    private static final String TAG_ROOT = "rss";
    private static final String TAG_CHANNEL = "channel";
    private final ElementFinder<Channel> channelFinder;

    public PodcastIntigator(ElementFinder<Channel> channelFinder, ParseFinishWatcher parseFinishWatcher) {
        super(channelFinder, TAG_CHANNEL, parseFinishWatcher);
        this.channelFinder = channelFinder;
    }

    @Override
    public String getRootTag() {
        return TAG_ROOT;
    }

    @Override
    public Channel getResult() {
        return channelFinder.getResult();
    }

}
