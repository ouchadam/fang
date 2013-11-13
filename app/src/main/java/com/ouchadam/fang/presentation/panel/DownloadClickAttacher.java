package com.ouchadam.fang.presentation.panel;

import android.view.View;

import com.ouchadam.fang.domain.FullItem;

class DownloadClickAttacher {

    private final SlidingPanelViewManipulator.OnDownloadClickListener onDownloadClickedListener;

    private boolean isDownloaded = false;
    private FullItem item;

    public DownloadClickAttacher(DownloadController downloadController, SlidingPanelViewManipulator.OnDownloadClickListener onDownloadClickedListener) {
        this.onDownloadClickedListener = onDownloadClickedListener;
        downloadController.setListener(onDownloadClicked);
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

    private final View.OnClickListener onDownloadClicked = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            onDownloadClickedListener.onDownloadClicked(item);
        }
    };
}
