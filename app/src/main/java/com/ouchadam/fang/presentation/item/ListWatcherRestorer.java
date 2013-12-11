package com.ouchadam.fang.presentation.item;

import android.app.Activity;

import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;

public class ListWatcherRestorer implements ActivityCallback {

    private boolean hasRestored;
    private Downloader downloader;

    ListWatcherRestorer() {
        this.hasRestored = false;
    }

    @Override
    public void onAttach(Activity activity) {
        this.downloader = Classes.from(activity);
    }

    public void restoreWatcher(ListItemWatcher.ItemWatcher listWatcher) {
        if (!hasRestored) {
            downloader.restore(new LazyListItemWatcher(listWatcher));
            hasRestored = true;
        }
    }
}
