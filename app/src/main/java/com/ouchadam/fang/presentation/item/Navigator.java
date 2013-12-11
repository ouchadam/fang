package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.presentation.controller.ChannelActivity;
import com.ouchadam.fang.presentation.controller.TopTenActivity;
import com.ouchadam.fang.presentation.topten.TopTenType;

public class Navigator {

    private final Context context;

    public Navigator(Context context) {
        this.context = context;
    }

    public void toChannel(String channelTitle) {
        Intent intent = new Intent(context, ChannelActivity.class);
        intent.putExtra(ChannelActivity.EXTRA_CHANNEL, channelTitle);
        context.startActivity(intent);
    }

    public void toTopTen(TopTenType topTenType) {
        Intent intent = new Intent(context, TopTenActivity.class);
        intent.putExtra(TopTenActivity.EXTRA_TOP_TEN_TYPE, topTenType.name());
        context.startActivity(intent);
    }
}
