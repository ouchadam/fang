package com.ouchadam.fang.parsing.podcast;

import com.novoda.sexp.RootTag;
import com.novoda.sexp.SimpleTagInstigator;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.parsing.InstigatorResult;

class PodcastInstigator extends SimpleTagInstigator implements InstigatorResult<Channel> {

    private static final String TAG_ROOT = "rss";
    private static final String TAG_CHANNEL = "channel";
    private final ElementFinder<Channel> channelFinder;

    public PodcastInstigator(ElementFinder<Channel> channelFinder, ParseFinishWatcher parseFinishWatcher) {
        super(channelFinder, TAG_CHANNEL, parseFinishWatcher);
        this.channelFinder = channelFinder;
    }

    @Override
    public RootTag getRootTag() {
        return RootTag.create(TAG_ROOT);
    }

    @Override
    public Channel getResult() {
        return channelFinder.getResult();
    }

}
