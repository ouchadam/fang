package com.ouchadam.fang.debug;

import android.content.ContentResolver;

import com.ouchadam.fang.Log;
import com.ouchadam.fang.parsing.podcast.ChannelFinder;
import com.ouchadam.fang.parsing.podcast.PodcastParser;
import com.ouchadam.fang.persistance.ChannelPersister;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.item.DatabaseCounter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

class FeedDownloader {

    private final ThreadExecutor threadExecutor;
    private final ThreadTracker threadTracker;
    private final ContentResolver contentResolver;
    private SyncDataHandler.SyncError syncError;

    FeedDownloader(ThreadExecutor threadExecutor, ThreadTracker threadTracker, ContentResolver contentResolver, SyncDataHandler.SyncError syncError) {
        this.threadExecutor = threadExecutor;
        this.threadTracker = threadTracker;
        this.contentResolver = contentResolver;
        this.syncError = syncError;
    }

    public void download(final Feed feed) {
        threadExecutor.run(new Runnable() {
            @Override
            public void run() {
                getPodcastFrom(feed, threadTracker);
            }
        });
    }

    private void getPodcastFrom(Feed feed, ThreadTracker threadTracker) {
        int currentItemCount = getCurrentItemCount(feed) - feed.oldItemCount;
        PodcastParser podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance());
        try {
            Log.d("Fetching : " + feed.url);
            InputStream urlInputStream = getInputStreamFrom(feed.url);
            podcastParser.parse(urlInputStream);
            new ChannelPersister(contentResolver).persist(podcastParser.getResult(), feed.url, currentItemCount);
            Log.d("Fetched : " + feed.url);
        } catch (IOException e) {
            syncError.onError(e);
        }
        threadTracker.threadFinished();
    }

    private int getCurrentItemCount(Feed feed) {
        if (!feed.hasChannelTitle()) {
            return 0;
        }
        return new DatabaseCounter(
                contentResolver,
                Uris.FULL_ITEM,
                new String[]{Tables.Item.ITEM_CHANNEL.name()},
                Tables.Channel.CHANNEL_TITLE.name() + "=?",
                new String[]{feed.channelTitle}
        ).getCurrentCount();
    }

    private InputStream getInputStreamFrom(String url) throws IOException {
        URL urlForStream = new URL(url);
        return urlForStream.openStream();
    }

}
