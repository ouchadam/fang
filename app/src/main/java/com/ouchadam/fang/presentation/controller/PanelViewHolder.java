package com.ouchadam.fang.presentation.controller;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class PanelViewHolder {

    private final PositionController positionController;
    private final MediaViewController mediaController;
    private final DownloadController downloadController;

    public static PanelViewHolder from(SlidingUpPanelLayout panel) {
        ViewSwitcher topMediaSwitcher = Views.findById(panel, R.id.media_switcher);
        ViewSwitcher bottomMediaSwitcher = Views.findById(panel, R.id.bottom_media_switcher);

        ImageButton playTop = Views.findById(panel, R.id.play_top);
        ImageButton playBottom = Views.findById(panel, R.id.play_bottom);
        ImageButton pauseTop = Views.findById(panel, R.id.pause_top);
        ImageButton pauseBottom = Views.findById(panel, R.id.pause_bottom);

        MediaViewController mediaController = new MediaViewController(playTop, playBottom, pauseTop, pauseBottom, topMediaSwitcher, bottomMediaSwitcher);

        Button downloadButton = Views.findById(panel, R.id.download);
        DownloadController downloadController = new DownloadController(downloadButton);

        SeekBar seekBar = Views.findById(panel, R.id.seek_bar);
        TextView currentTime = Views.findById(panel, R.id.current_position);
        TextView endTime = Views.findById(panel, R.id.total_time);
        PositionController position = new PositionController(seekBar, currentTime, endTime);

        return new PanelViewHolder(position, mediaController, downloadController);
    }

    PanelViewHolder(PositionController positionController, MediaViewController mediaController, DownloadController downloadController) {
        this.mediaController = mediaController;
        this.positionController = positionController;
        this.downloadController = downloadController;
    }

    public boolean isInPlayMode() {
        return mediaController.isInPlayMode();
    }

    public MediaViewController mediaController() {
        return mediaController;
    }

    public void showCollapsed(boolean isDownloaded) {
        mediaController.showCollapsed(isDownloaded);
        downloadController.panelScopeChange(isDownloaded);
    }

    public void showExpanded(boolean downloaded) {
        mediaController.showExpanded(downloaded);
        downloadController.panelScopeChange(downloaded);
    }

    public DownloadController downloadController() {
        return downloadController;
    }

    public PositionController positionController() {
        return positionController;
    }

}
