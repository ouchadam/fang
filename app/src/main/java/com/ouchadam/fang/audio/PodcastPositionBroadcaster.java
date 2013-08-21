package com.ouchadam.fang.audio;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;

class PodcastPositionBroadcaster extends FangBroadcaster<PodcastPosition> {

    public PodcastPositionBroadcaster(Context context) {
        super(context);
    }

    @Override
    protected Intent marshall(PodcastPosition what) {
        return new Intent("postion_action");
    }
}
