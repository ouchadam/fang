package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DownloadController implements PanelComponent {

    private final ImageButton downloadButton;

    DownloadController(ImageButton downloadButton) {
        this.downloadButton = downloadButton;
    }

    @Override
    public void showExpanded(boolean isDownloaded) {
        panelScopeChange(isDownloaded);
    }

    @Override
    public void showCollapsed(boolean isDownloaded) {
        panelScopeChange(isDownloaded);
    }

    private int getVisibilityFrom(boolean downloaded) {
        return downloaded ? View.INVISIBLE : View.VISIBLE;
    }

    public void setListener(View.OnClickListener onDownloadClicked) {
        downloadButton.setOnClickListener(onDownloadClicked);
    }

    private void panelScopeChange(boolean downloaded) {
        downloadButton.setVisibility(getVisibilityFrom(downloaded));
    }

    public void update(boolean isDownloaded) {
        panelScopeChange(isDownloaded);
    }
}
