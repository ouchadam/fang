package com.ouchadam.fang.presentation.item;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;

class LazyListItemWatcher implements LazyWatcher {

    private final ListItemWatcher.ItemWatcher itemWatcher;

    LazyListItemWatcher(ListItemWatcher.ItemWatcher itemWatcher) {
        this.itemWatcher = itemWatcher;
    }

    @Override
    public DownloadWatcher create(DownloadId downloadId, long itemId) {
        return new ListItemWatcher(itemWatcher, itemId, downloadId);
    }

}
