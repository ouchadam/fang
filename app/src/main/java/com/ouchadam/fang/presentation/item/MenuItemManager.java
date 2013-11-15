package com.ouchadam.fang.presentation.item;

import android.app.Activity;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;

class MenuItemManager implements ActivityCallback {

    private ActionBarRefresher actionBarRefresher;
    private boolean isDownloading;
    private boolean isDownloaded;

    MenuItemManager() {
        this.isDownloading = false;
        this.isDownloaded = false;
    }

    @Override
    public void onAttach(Activity activity) {
        this.actionBarRefresher = Classes.from(activity);
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
        refresh();
    }

    public void setDownloaded(boolean isDownloaded) {
        this.isDownloaded = isDownloaded;
        refresh();
    }

    private void refresh() {
        if (actionBarRefresher == null) {
            throw new IllegalAccessError("Must call onAttach");
        }
        actionBarRefresher.refresh();
    }

    public boolean isDownloading() {
        return isDownloading;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }
}
