package com.ouchadam.fang.presentation.item;

import android.content.Context;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.AsyncNotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

public class ItemDownloader {

    private static final int NEXT_POSITION_OFFSET = 1;
    private final Downloader downloader;
    private final Context context;
    private final DatabaseCounter databaseCounter;

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
        downloader.watch(downloadId);
        AsyncNotificationWatcher asyncNotificationWatcher = new AsyncNotificationWatcher(context, downloadable, downloadId);
        asyncNotificationWatcher.startWatching();
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
