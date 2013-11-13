package com.ouchadam.fang.audio.event;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;

public class PodcastPlayerEventBroadcaster extends FangBroadcaster<PlayerEvent> {

    public PodcastPlayerEventBroadcaster(Context context) {
        super(context);
    }

    @Override
    protected Intent marshall(PlayerEvent what) {
        return new PlayerEventIntentMarshaller(PlayerEvent.Event.ACTION_PREFIX).to(what.getId(), what);
    }

}
