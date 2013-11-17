package com.ouchadam.fang.presentation.item;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.debug.ChannelFeedDownloadService;
import com.ouchadam.fang.presentation.controller.PullToRefreshExposer;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

class ChannelRefreshCompleteReceiver extends BroadcastReceiver {

    private final PullToRefreshExposer pullToRefreshExposer;

    ChannelRefreshCompleteReceiver(PullToRefreshExposer pullToRefreshExposer) {
        this.pullToRefreshExposer = pullToRefreshExposer;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ChannelFeedDownloadService.ACTION_CHANNEL_FEED_COMPLETE.equals(intent.getAction())) {
            pullToRefreshExposer.setRefreshComplete();
        }
    }
}
