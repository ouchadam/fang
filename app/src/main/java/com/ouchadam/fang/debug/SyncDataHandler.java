package com.ouchadam.fang.debug;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.item.LastUpdatedManager;

import java.io.IOException;
import java.util.List;

public class SyncDataHandler {

    private static final int NOTIFICATION_ID = 0xAC;

    private final NotificationManager notificationManager;
    private final ContentResolver contentResolver;
    private final Context context;
    private final ThreadTracker.OnAllThreadsComplete threadsCompleteListener;
    private final SyncError syncError;

    public interface SyncError {
        void onError(IOException e);
    }

    public static SyncDataHandler from(Context context, ThreadTracker.OnAllThreadsComplete onAllThreadsComplete, SyncError syncError) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        ContentResolver contentResolver = context.getContentResolver();
        return new SyncDataHandler(notificationManager, contentResolver, context, onAllThreadsComplete, syncError);
    }

    SyncDataHandler(NotificationManager notificationManager, ContentResolver contentResolver, Context context, ThreadTracker.OnAllThreadsComplete threadsCompleteListener, SyncError syncError) {
        this.notificationManager = notificationManager;
        this.contentResolver = contentResolver;
        this.context = context;
        this.threadsCompleteListener = threadsCompleteListener;
        this.syncError = syncError;
    }

    public void handleSync(Bundle extras) {
        Log.e("Asked to handle Sync");
        FeedServiceInfo serviceInfo = FeedServiceInfo.from(extras);
        List<Feed> feeds = getFeeds(serviceInfo);
        if (feeds.isEmpty()) {
            Log.e("No feeds, ending sync");
            syncError.onError(new IOException());
            return;
        } else {
            startSync(serviceInfo, feeds);
        }
    }

    private List<Feed> getFeeds(FeedServiceInfo serviceInfo) {
        switch (serviceInfo.getType()) {
            case ADD:
                return serviceInfo.getUrlsToAdd();

            case REFRESH:
                return getSubscribedChannelUrls();

            default:
                syncError.onError(new IOException("Refresh type not handled"));
                return Collections.newArrayList();
        }
    }

    private void startSync(FeedServiceInfo serviceInfo, List<Feed> feeds) {
        showNotification(serviceInfo.getType());
        downloadAndPersistPodcastFeeds(feeds);
    }

    private void showNotification(FeedServiceInfo.Type type) {
        Notification notification = new NotificationCompat.Builder(context).setSmallIcon(R.drawable.stat_notify_sync).setContentTitle(getNotificationTitle(type)).build();
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private String getNotificationTitle(FeedServiceInfo.Type type) {
        return type == FeedServiceInfo.Type.ADD ? "Adding podcast feed" : "Updating podcast feeds";
    }

    private List<Feed> getSubscribedChannelUrls() {
        Query query = getQueryValues();
        Cursor cursor = contentResolver.query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder);

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

    private void downloadAndPersistPodcastFeeds(List<Feed> feeds) {
        ThreadTracker threadTracker = new ThreadTracker(feeds.size(), innerThreadListener);
        FeedDownloader feedDownloader = new FeedDownloader(new ThreadExecutor(), threadTracker, contentResolver, syncError);

        for (Feed feed : feeds) {
            feedDownloader.download(feed);
        }
    }

    private final ThreadTracker.OnAllThreadsComplete innerThreadListener = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
            // TODO show notification with how many new items
            LastUpdatedManager.from(context).setLastUpdated();
            dismissNotification();
            context.sendBroadcast(new Intent(ChannelFeedDownloadService.ACTION_CHANNEL_FEED_COMPLETE));
            threadsCompleteListener.onFinish();
        }
    };

    private void dismissNotification() {
        notificationManager.cancel(NOTIFICATION_ID);
    }
}
