package com.ouchadam.fang.sync;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class FangSyncLifecycle {

    private SyncReceiver syncReceiver;

    public void init(Activity activity, PullToRefreshExposer pullToRefreshExposer) {
        FangSyncHelper.createSyncAccount(activity);
        syncReceiver = new SyncReceiver(pullToRefreshExposer);
    }

    public void onResume(Context context) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("refreshing");
        intentFilter.addAction("complete");
        context.registerReceiver(syncReceiver, intentFilter);
    }

    public void onPause(Context context) {
        context.unregisterReceiver(syncReceiver);
    }

    private static class SyncReceiver extends BroadcastReceiver {

        private final PullToRefreshExposer pullToRefreshExposer;

        private SyncReceiver(PullToRefreshExposer pullToRefreshExposer) {
            this.pullToRefreshExposer = pullToRefreshExposer;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (intent != null && action != null) {
                if (action.equals("refreshing")) {
                    pullToRefreshExposer.setRefreshing();
                } else if (action.equals("complete")) {
                    pullToRefreshExposer.refreshComplete();
                }

            }
        }
    }

}
