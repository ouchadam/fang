package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.presentation.controller.ChannelActivity;

class Navigator {

    private final Context context;

    Navigator(Context context) {
        this.context = context;
    }

    public void toChannel(String channelTitle) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CHANNEL, channelTitle);
        context.startActivity(intent);
    }

}
