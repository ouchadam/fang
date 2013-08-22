package com.ouchadam.fang.presentation.controller;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;

public class PodcastPlayerEventBroadcaster extends FangBroadcaster<PlayerEvent> {

    public PodcastPlayerEventBroadcaster(Context context) {
        super(context);
    }

    @Override
    protected Intent marshall(PlayerEvent what) {
        return new PlayerEventIntentMarshaller().to(what.getId(), what);
    }

}
