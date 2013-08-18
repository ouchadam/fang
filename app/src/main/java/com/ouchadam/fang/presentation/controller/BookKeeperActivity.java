package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.RestoreableBookKeeper;
import com.ouchadam.bookkeeper.delegate.IdManager;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;

public class BookKeeperActivity extends FragmentActivity implements Downloader {

    private RestoreableBookKeeper bookKeeper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBookKeeper();
    }

    private void initBookKeeper() {
        bookKeeper = RestoreableBookKeeper.newInstance(this);
    }

    @Override
    public DownloadId keep(Downloadable from) {
        return bookKeeper.keep(from);
    }

    @Override
    public void restore(final LazyWatcher lazyWatcher) {
        bookKeeper.restore(new IdManager.BookKeeperRestorer() {
            @Override
            public void onRestore(DownloadId downloadId, long itemId) {
                DownloadWatcher downloadWatcher = lazyWatcher.create(downloadId, itemId);
                bookKeeper.watch(downloadId, downloadWatcher);
            }
        });
    }

    @Override
    public void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers) {
        bookKeeper.watch(downloadId, downloadWatchers);
    }

    @Override
    public void store(DownloadId downloadId, long itemId) {
        bookKeeper.store(downloadId, itemId);
    }

}
