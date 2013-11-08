package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class DownloadController {

    private final ImageButton downloadButton;

    DownloadController(ImageButton downloadButton) {
        this.downloadButton = downloadButton;
    }

    public void panelScopeChange(boolean downloaded) {
        downloadButton.setVisibility(getVisibilityFrom(downloaded));
    }

    private int getVisibilityFrom(boolean downloaded) {
        return downloaded ? View.INVISIBLE : View.VISIBLE;
    }

    public void setListener(View.OnClickListener onDownloadClicked) {
        downloadButton.setOnClickListener(onDownloadClicked);
    }
}
