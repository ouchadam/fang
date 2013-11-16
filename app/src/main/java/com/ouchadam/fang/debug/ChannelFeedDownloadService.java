package com.ouchadam.fang.debug;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.R;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.item.LastUpdatedManager;

import java.util.List;

public class ChannelFeedDownloadService extends Service {

    public static final String ACTION_CHANNEL_FEED_COMPLETE = "channelFeedComplete";
    private static final int NOTIFICATION_ID = 0xAC;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            List<Feed> feeds = Collections.newArrayList();
            FeedServiceInfo from = FeedServiceInfo.from(intent.getExtras());
            switch (from.getType()) {
                case ADD:
                    feeds = from.getUrlsToAdd();
                    break;
                case REFRESH:
                    feeds = getSubscribedChannelUrls();
                    break;
            }

            if (!feeds.isEmpty()) {
                showNotification(from.getType());
                downloadAndPersistPodcastFeeds(feeds);
            }
            return START_STICKY;
        }
        return START_NOT_STICKY;
    }

    private void showNotification(FeedServiceInfo.Type type) {
        Notification notification = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.stat_notify_sync).setContentTitle(getNotificationTitle(type)).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private String getNotificationTitle(FeedServiceInfo.Type type) {
        return type == FeedServiceInfo.Type.ADD ? "Adding podcast feed" : "Updating podcast feeds";
    }

    private List<Feed> getSubscribedChannelUrls() {
        Query query = getQueryValues();
        Cursor cursor = getContentResolver().query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder);

        List<Feed> feeds = Collections.newArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Feed feed = new Feed();
                feed.url = cursor.getString(cursor.getColumnIndex(Tables.Channel.URL.name()));
                feed.channelTitle = cursor.getString(cursor.getColumnIndex(Tables.Channel.CHANNEL_TITLE.name()));
                feed.oldItemCount = cursor.getInt(cursor.getColumnIndex(Tables.Channel.NEW_ITEM_COUNT.name()));
                feeds.add(feed);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return feeds;
    }

    private Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.CHANNEL)).build();
    }

    public void downloadAndPersistPodcastFeeds(List<Feed> feeds) {
        ThreadTracker threadTracker = new ThreadTracker(feeds.size(), threadsCompleteListener);
        FeedDownloader feedDownloader = new FeedDownloader(new ThreadExecutor(), threadTracker, getContentResolver());

        for (Feed feed : feeds) {
            feedDownloader.download(feed);
        }
    }

    private final ThreadTracker.OnAllThreadsComplete threadsCompleteListener = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
            // TODO show notification with how many new items
            LastUpdatedManager.from(ChannelFeedDownloadService.this).setLastUpdated();
            dismissNotification();
            sendBroadcast(new Intent(ACTION_CHANNEL_FEED_COMPLETE));
            stopSelf();
        }
    };

    private void dismissNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

    }
}
