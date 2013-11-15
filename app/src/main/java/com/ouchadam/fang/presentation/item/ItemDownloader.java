package com.ouchadam.fang.presentation.item;

import android.content.Context;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.AsyncNotificationWatcher;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

import java.util.Arrays;
import java.util.List;

public class ItemDownloader {

    private static final int NEXT_POSITION_OFFSET = 1;
    private final Downloader downloader;
    private final Context context;
    private final DatabaseCounter databaseCounter;
    private List<LazyWatcher> lazyWatchers;

    public ItemDownloader(Downloader downloader, Context context) {
        this.downloader = downloader;
        this.context = context;
        databaseCounter = new DatabaseCounter(context.getContentResolver(), Uris.PLAYLIST, new String[]{Tables.Playlist.ITEM_ID.name()}, null, null);
    }

    public void downloadItem(Item item) {
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        addToPlaylist(item, downloadId);
        downloader.store(downloadId, item.getId());
        downloader.watch(downloadId, buildWatchers(lazyWatchers, downloadId, item.getId()));
        AsyncNotificationWatcher asyncNotificationWatcher = new AsyncNotificationWatcher(context, downloadable, downloadId);
        asyncNotificationWatcher.startWatching();
    }

    private DownloadWatcher[] buildWatchers(List<LazyWatcher> lazyWatchers, DownloadId downloadId, long itemId) {
        DownloadWatcher[] downloadWatchers = new DownloadWatcher[lazyWatchers.size()];
        int index = 0;
        for (LazyWatcher lazyWatcher : lazyWatchers) {
            downloadWatchers[index] = lazyWatcher.create(downloadId, itemId);
            index ++;
        }
        return downloadWatchers;
    }

    public void setWatchers(LazyWatcher... lazyWatchers) {
        this.lazyWatchers = Arrays.asList(lazyWatchers);
    }

    private void addToPlaylist(final Item item, final DownloadId downloadId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int listPosition = getPlaylistTotal() + NEXT_POSITION_OFFSET;
                new AddToPlaylistPersister(context.getContentResolver()).persist(ItemToPlaylist.from(item, downloadId.value(), listPosition));
            }
        }).start();
    }

    private int getPlaylistTotal() {
        return databaseCounter.getCurrentCount();
    }

}
