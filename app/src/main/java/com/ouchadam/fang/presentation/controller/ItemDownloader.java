package com.ouchadam.fang.presentation.controller;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

public class ItemDownloader {

    private final Downloader downloader;
    private final Context context;

    public ItemDownloader(Downloader downloader, Context context) {
        this.downloader = downloader;
        this.context = context;
    }

    public void downloadItem(Item item) {
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        downloader.store(downloadId, item.getId());

        addToPlaylist(item, downloadId);
        downloader.watch(downloadId, new NotificationWatcher(context, downloadable, downloadId));
    }

    private void addToPlaylist(Item item, DownloadId downloadId) {
        int listPosition = getListPosition() + 1;
        new AddToPlaylistPersister(context.getContentResolver()).persist(ItemToPlaylist.from(item, downloadId.value(), listPosition));
    }

    private int getListPosition() {

        return new PlaylistQuery(context.getContentResolver()).getCurrentCount();
    }

    private static class PlaylistQuery {

        private final ContentResolver contentResolver;

        private PlaylistQuery(ContentResolver contentResolver) {
            this.contentResolver = contentResolver;
        }

        public int getCurrentCount() {

            Cursor cursor = contentResolver.query(
                    FangProvider.getUri(Uris.PLAYLIST),
                    new String[]{Tables.Playlist.ITEM_ID.name()},
                    null,
                    null,
                    null
            );

            if (cursor != null && cursor.moveToFirst()) {
                return cursor.getCount();
            } else {
                return 0;
            }

        }
    }

}
