package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.Button;

public class DownloadController {

    private final Button downloadButton;

    DownloadController(Button downloadButton) {
        this.downloadButton = downloadButton;
    }

    public void panelScopeChange(boolean downloaded) {
        if (downloaded) {
            downloadButton.setVisibility(View.INVISIBLE);
        } else {
            downloadButton.setVisibility(View.VISIBLE);
        }
    }

    public void setListener(View.OnClickListener onDownloadClicked) {
        downloadButton.setOnClickListener(onDownloadClicked);
    }
}
