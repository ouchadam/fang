package com.ouchadam.fang.debug;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.novoda.notils.java.Collections;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.parsing.ChannelFinder;
import com.ouchadam.fang.parsing.PodcastParser;
import com.ouchadam.fang.persistance.ChannelPersister;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ChannelFeedDownloadService extends Service {

    private static final int NOTIFICATION_ID = 0xAC;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<String> urls = Collections.newArrayList();
        FeedServiceInfo from = FeedServiceInfo.from(intent.getExtras());
        switch (from.getType()) {
            case ADD :
                urls = from.getUrlsToAdd();
                break;
            case REFRESH:
                urls = getSubscribedChannelUrls();
                break;
        }

        if (!urls.isEmpty()) {
            showNotification();
            downloadAndPersistPodcastFeeds(urls);
        }
        return START_STICKY;
    }

    private void showNotification() {
        Log.e("!!!", "Showing notification");
        Notification notification = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.play_button).setContentTitle("Refreshing podcast feeds").build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_ID, notification);
    }

    private List<String> getSubscribedChannelUrls() {
        Query query = getQueryValues();
        Cursor cursor = getContentResolver().query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder);

        List<String> urls = Collections.newArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                urls.add(cursor.getString(cursor.getColumnIndex(Tables.Channel.URL.name())));
            }
        }
        return urls;
    }

    private Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.CHANNEL)).build();
    }

    public void downloadAndPersistPodcastFeeds(List<String> urls) {
        // TODO Move to thread pool
        final ThreadTracker threadTracker = new ThreadTracker(urls.size(), threadsCompleteListener);
        for (final String url : urls) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getPodcastFrom(url, threadTracker);
                }
            }).start();
        }
    }

    private void getPodcastFrom(String url, ThreadTracker threadTracker) {
        PodcastParser podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), parseFinishWatcher);
        try {
            Log.e("!!!", "Fetching : " + url);
            InputStream urlInputStream = getInputStreamFrom(url);
            podcastParser.parse(urlInputStream);
            new ChannelPersister(getContentResolver()).persist(podcastParser.getResult(), url);
            Log.e("!!!", "Fetched : " + url);
            // TODO broadcast channel?
        } catch (IOException e) {
            broadcastFailure(e.getMessage());
        }
        threadTracker.threadFinished();
    }

    private InputStream getInputStreamFrom(String url) throws IOException {
        URL urlForStream = new URL(url);
        return urlForStream.openStream();
    }

    private void broadcastFailure(String failureMessage) {
        // TODO
    }

    private static class ThreadTracker {

        private final OnAllThreadsComplete onAllThreadsComplete;
        private int threadCount;

        interface OnAllThreadsComplete {
            void onFinish();
        }

        public ThreadTracker(int threadCount, OnAllThreadsComplete onAllThreadsComplete) {
            this.threadCount = threadCount;
            this.onAllThreadsComplete = onAllThreadsComplete;
        }

        public synchronized void threadFinished() {
            if (--threadCount == 0) {
                onAllThreadsComplete.onFinish();
            }
        }

    }

    private final ThreadTracker.OnAllThreadsComplete threadsCompleteListener = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
            dismissNotification();
            stopSelf();
        }
    };

    private final ParseFinishWatcher parseFinishWatcher = new ParseFinishWatcher() {
        @Override
        public void onFinish() {
        }
    };

    private void dismissNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(NOTIFICATION_ID);

    }
}
