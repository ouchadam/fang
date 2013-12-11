package com.ouchadam.fang.presentation.item;

import android.content.Context;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.bookkeeper.watcher.notification.AsyncNotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;

import java.util.Arrays;
import java.util.List;

public class ItemDownloader {

    private final Downloader downloader;
    private final Context context;
    private List<LazyWatcher> lazyWatchers;

    public ItemDownloader(Downloader downloader, Context context) {
        this.downloader = downloader;
        this.context = context;
    }

    public void downloadItem(Item item) {
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        addToPlaylist(context, item, downloadId);
        downloader.store(downloadId, item.getId());
        downloader.watch(downloadId, buildWatchers(lazyWatchers, downloadId, item.getId()));
        AsyncNotificationWatcher asyncNotificationWatcher = new AsyncNotificationWatcher(context, downloadable, downloadId);
        asyncNotificationWatcher.startWatching();
    }

    private void addToPlaylist(Context context, Item item, DownloadId downloadId) {
        ItemToPlaylist itemToPlaylist = ItemToPlaylist.from(item, downloadId.value());
        PlaylistAddService.start(context, itemToPlaylist);
    }

    private DownloadWatcher[] buildWatchers(List<LazyWatcher> lazyWatchers, DownloadId downloadId, long itemId) {
        DownloadWatcher[] downloadWatchers = new DownloadWatcher[lazyWatchers.size()];
        int index = 0;
        for (LazyWatcher lazyWatcher : lazyWatchers) {
            downloadWatchers[index] = lazyWatcher.create(downloadId, itemId);
            index++;
        }
        return downloadWatchers;
    }

    public void setWatchers(LazyWatcher... lazyWatchers) {
        this.lazyWatchers = Arrays.asList(lazyWatchers);
    }

}
