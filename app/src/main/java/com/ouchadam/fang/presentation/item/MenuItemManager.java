package com.ouchadam.fang.presentation.item;

import android.app.Activity;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;

class MenuItemManager implements ActivityCallback {

    private ActionBarRefresher actionBarRefresher;
    private boolean isDownloading;
    private boolean isDownloaded;
    private long itemId;

    MenuItemManager() {
        this.isDownloading = false;
        this.isDownloaded = false;
        this.itemId = -1;
    }

    @Override
    public void onAttach(Activity activity) {
        this.actionBarRefresher = Classes.from(activity);
    }

    public void setDownloading(boolean isDownloading) {
        this.isDownloading = isDownloading;
        refresh();
    }

    public void initFrom(FullItem fullItem) {
        this.itemId = fullItem.getItemId();
        setDownloaded(fullItem.isDownloaded());
    }

    private void setDownloaded(boolean isDownloaded) {
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

    public long currentId() {
        return itemId;
    }
}
