package com.ouchadam.fang.presentation.panel;

import android.view.View;

import com.ouchadam.fang.domain.FullItem;

class DownloadFoo {

    private final DownloadController downloadController;
    private boolean isDownloaded = false;
    private FullItem item;

    public DownloadFoo(DownloadController downloadController) {
        this.downloadController = downloadController;
    }

    public void itemChange(FullItem item) {
        this.item = item;
        isDownloaded = isDownloaded(item);
    }

    private boolean isDownloaded(FullItem item) {
        return item != null && item.isDownloaded();
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setListener(final SlidingPanelViewManipulator.OnDownloadClickListener onDownloadClickedListener) {
        downloadController.setListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDownloadClickedListener.onDownloadClicked(item);
            }
        });
    }
}
