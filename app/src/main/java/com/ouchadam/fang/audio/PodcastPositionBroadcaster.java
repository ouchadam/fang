package com.ouchadam.fang.audio;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;

class PodcastPositionBroadcaster extends FangBroadcaster<PodcastPosition> {

    public static final String POSITION_ACTION = "postion_action";
    public static final String EXTRA_POSITION = "position_extra";

    public PodcastPositionBroadcaster(Context context) {
        super(context);
    }

    @Override
    protected Intent marshall(PodcastPosition what) {
        Intent intent = new Intent(POSITION_ACTION);
        intent.putExtra(EXTRA_POSITION, what);
        return intent;
    }
}
