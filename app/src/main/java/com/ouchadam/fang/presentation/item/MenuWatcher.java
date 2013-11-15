package com.ouchadam.fang.presentation.item;

import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;

class MenuWatcher implements DownloadWatcher {

    private final DetailsFragment.MenuItemManager menuItemManager;
    private final DownloadId downloadId;

    MenuWatcher(DetailsFragment.MenuItemManager menuItemManager, DownloadId downloadId) {
        this.menuItemManager = menuItemManager;
        this.downloadId = downloadId;
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    @Override
    public void onStart() {
        menuItemManager.setDownloading(true);
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
    }

    @Override
    public void onStop() {
        menuItemManager.setDownloading(false);
    }

    static class LazyMenuWatcher implements LazyWatcher {

        private final DetailsFragment.MenuItemManager menuItemManager;

        LazyMenuWatcher(DetailsFragment.MenuItemManager menuItemManager) {
            this.menuItemManager = menuItemManager;
        }

        @Override
        public DownloadWatcher create(DownloadId downloadId, long l) {
            return new MenuWatcher(menuItemManager, downloadId);
        }
    }

}
