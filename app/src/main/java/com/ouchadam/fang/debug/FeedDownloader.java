package com.ouchadam.fang.debug;

import android.content.ContentResolver;
import android.util.Log;

import com.ouchadam.fang.parsing.ChannelFinder;
import com.ouchadam.fang.parsing.PodcastParser;
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

    FeedDownloader(ThreadExecutor threadExecutor, ThreadTracker threadTracker, ContentResolver contentResolver) {
        this.threadExecutor = threadExecutor;
        this.threadTracker = threadTracker;
        this.contentResolver = contentResolver;
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
            Log.e("!!!", "Fetching : " + feed.url);
            InputStream urlInputStream = getInputStreamFrom(feed.url);
            podcastParser.parse(urlInputStream);
            new ChannelPersister(contentResolver).persist(podcastParser.getResult(), feed.url, currentItemCount);
            Log.e("!!!", "Fetched : " + feed.url);
        } catch (IOException e) {
            broadcastFailure(e.getMessage());
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

    private void broadcastFailure(String failureMessage) {
        // TODO
    }

}
