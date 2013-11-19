package com.ouchadam.fang.debug;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.ouchadam.fang.presentation.item.LastUpdatedManager;

public class ChannelFeedDownloadService extends Service {

    public static final String ACTION_CHANNEL_FEED_COMPLETE = "channelFeedComplete";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            SyncDataHandler.from(this, onAllThreadsComplete).handleSync(intent.getExtras(), null);
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private final ThreadTracker.OnAllThreadsComplete onAllThreadsComplete = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
                stopSelf();
        }
    };

}
