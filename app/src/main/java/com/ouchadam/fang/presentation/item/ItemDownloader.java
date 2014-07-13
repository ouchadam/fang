package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.text.TextUtils;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.bookkeeper.watcher.notification.AsyncNotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;

import java.util.Arrays;
import java.util.List;

public class ItemDownloader {

    private final Downloader downloader;
    private final Context context;
    private final LinkValidator linkValidator;
    private List<LazyWatcher> lazyWatchers;

    public ItemDownloader(Downloader downloader, Context context) {
        this.downloader = downloader;
        this.context = context;
        this.linkValidator = new LinkValidator();
    }

    public void downloadItem(Item item) throws LinkValidator.BadLinkException {
        linkValidator.validateOrThrow(item.getAudio().getUrl());
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        addToPlaylist(context, item, downloadId);
        downloader.store(downloadId, item.getId());
        watchDownload(item, downloadId);
        AsyncNotificationWatcher asyncNotificationWatcher = new AsyncNotificationWatcher(context, downloadable, downloadId);
        asyncNotificationWatcher.startWatching();
    }

    private void watchDownload(Item item, DownloadId downloadId) {
        downloader.watch(downloadId, buildWatchers(lazyWatchers, downloadId, item.getId()));
    }

    private void addToPlaylist(Context context, Item item, DownloadId downloadId) {
        ItemToPlaylist itemToPlaylist = ItemToPlaylist.from(item, downloadId.value());
        PlaylistAddService.start(context, itemToPlaylist);
    }

    private DownloadWatcher[] buildWatchers(List<LazyWatcher> lazyWatchers, DownloadId downloadId, long itemId) {
        if (areValid(lazyWatchers)) {
            DownloadWatcher[] downloadWatchers = new DownloadWatcher[lazyWatchers.size()];
            int index = 0;
            for (LazyWatcher lazyWatcher : lazyWatchers) {
                downloadWatchers[index] = lazyWatcher.create(downloadId, itemId);
                index++;
            }
            return downloadWatchers;
        } else {
            return new DownloadWatcher[0];
        }
    }

    private boolean areValid(List<LazyWatcher> lazyWatchers) {
        return lazyWatchers != null && !lazyWatchers.isEmpty() && !lazyWatchers.contains(null);
    }

    public void setWatchers(LazyWatcher... lazyWatchers) {
        if (lazyWatchers != null) {
            this.lazyWatchers = Arrays.asList(lazyWatchers);
        }
    }

    public static class LinkValidator {

        public void validateOrThrow(String link) throws BadLinkException {
            Log.d("Attmepting to download : " + link);
            if (link == null || TextUtils.isEmpty(link)) {
                throw new BadLinkException(link);
            }
        }

        public static class BadLinkException extends Exception {
            private String link;

            public BadLinkException(String link) {
                super("bad download link, got : " + "\"" + link + "\"");
                this.link = link;
            }

            public String getLink() {
                return "\"" + link + "\"";
            }
        }
    }

}
