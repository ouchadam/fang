package com.ouchadam.fang.presentation.controller;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;

public class PodcastPlayerEventBroadcaster extends FangBroadcaster<PlayerEvent> {

    private final String prefix;

    public PodcastPlayerEventBroadcaster(String prefix, Context context) {
        super(context);
        this.prefix = prefix;
    }

    @Override
    protected Intent marshall(PlayerEvent what) {
        return new PlayerEventIntentMarshaller(prefix).to(what.getId(), what);
    }

}
