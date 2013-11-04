package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.Button;

public class DownloadController {

    private final Button downloadButton;

    DownloadController(Button downloadButton) {
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
