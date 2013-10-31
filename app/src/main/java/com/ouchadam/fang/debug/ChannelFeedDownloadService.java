package com.ouchadam.fang.debug;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.util.Log;

import com.novoda.notils.java.Collections;
import com.novoda.sexp.parser.ParseFinishWatcher;
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

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        List<String> urls = Collections.newArrayList();
        ServiceInfo from = ServiceInfo.from(intent.getExtras());
        switch (from.getType()) {
            case ADD :
                urls = from.getUrlsToAdd();
                break;
            case REFRESH:
                urls = getSubscribedChannelUrls();
                break;
        }

        if (!urls.isEmpty()) {
            downloadAndPersistPodcastFeeds(urls);
        }
        return START_STICKY;
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
        for (final String url : urls) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    getPodcastFrom(url);
                }
            }).start();
        }
    }

    private void getPodcastFrom(String url) {
        PodcastParser podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), parseFinishWatcher);

        try {
            InputStream urlInputStream = getInputStreamFrom(url);
            podcastParser.parse(urlInputStream);
            new ChannelPersister(getContentResolver()).persist(podcastParser.getResult(), url);
            // TODO broadcast channel?
        } catch (IOException e) {
            broadcastFailure(e.getMessage());
        }
    }

    private InputStream getInputStreamFrom(String url) throws IOException {
        Log.e("!!!", "Fetching stream for : " + url);
        URL urlForStream = new URL(url);
        return urlForStream.openStream();
    }

    private void broadcastFailure(String failureMessage) {
        // TODO
    }

    private final ParseFinishWatcher parseFinishWatcher = new ParseFinishWatcher() {
        @Override
        public void onFinish() {
            stopSelf();
        }
    };
}
