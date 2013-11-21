package com.ouchadam.fang.presentation.item;

import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;

class MenuWatcher implements DownloadWatcher {

    private final MenuItemManager menuItemManager;
    private final DownloadId downloadId;
    private final long itemId;

    MenuWatcher(MenuItemManager menuItemManager, DownloadId downloadId, long itemId) {
        this.menuItemManager = menuItemManager;
        this.downloadId = downloadId;
        this.itemId = itemId;
    }

    @Override
    public boolean isWatching(DownloadId downloadId) {
        return this.downloadId.equals(downloadId);
    }

    @Override
    public void onStart() {
        setDownloading(true);
    }

    @Override
    public void onUpdate(ProgressValues progressValues) {
        setDownloading(true);
    }

    @Override
    public void onStop() {
        setDownloading(false);
    }

    private void setDownloading(boolean isDownloading) {
        if (isViewingSameId()) {
            menuItemManager.setDownloading(isDownloading);
        }
    }

    private boolean isViewingSameId() {
        return menuItemManager.currentId() == itemId;
    }

    static class LazyMenuWatcher implements LazyWatcher {

        private final MenuItemManager menuItemManager;

        LazyMenuWatcher(MenuItemManager menuItemManager) {
            this.menuItemManager = menuItemManager;
        }

        @Override
        public DownloadWatcher create(DownloadId downloadId, long itemId) {
            return new MenuWatcher(menuItemManager, downloadId, itemId);
        }
    }

}
