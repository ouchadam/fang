package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.presentation.item.HeroHolder;
import com.ouchadam.fang.presentation.item.HeroManager;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class PanelViewHolder {

    private final PositionController positionController;
    private final MediaViewController mediaController;
    private final DownloadController downloadController;
    private final MainPanelController mainPanelController;

    public static PanelViewHolder from(SlidingUpPanelLayout panel) {
        ViewSwitcher topMediaSwitcher = Views.findById(panel, R.id.media_switcher);
        ViewSwitcher bottomMediaSwitcher = Views.findById(panel, R.id.bottom_media_switcher);

        View playTop = Views.findById(panel, R.id.play_top);
        View playBottom = Views.findById(panel, R.id.play_bottom);
        View pauseTop = Views.findById(panel, R.id.pause_top);
        View pauseBottom = Views.findById(panel, R.id.pause_bottom);

        MediaViewController mediaController = new MediaViewController(playTop, playBottom, pauseTop, pauseBottom, topMediaSwitcher, bottomMediaSwitcher);

        ImageButton downloadButton = Views.findById(panel, R.id.download);
        DownloadController downloadController = new DownloadController(downloadButton);

        SeekBar seekBar = Views.findById(panel, R.id.seek_bar);
        TextView currentTime = Views.findById(panel, R.id.current_position);
        TextView endTime = Views.findById(panel, R.id.total_time);
        PositionController position = new PositionController(seekBar, currentTime, endTime);

        DurationFormatter durationFormatter = new DurationFormatter(panel.getResources());

        ImageView heroView = Views.findById(panel, R.id.drawer_content_image);

        HeroManager heroManager = new HeroManager(new HeroHolder(), heroView, panel.getContext());
        MainPanelController mainPanelController = new MainPanelController(panel, durationFormatter, heroManager);

        return new PanelViewHolder(position, mediaController, downloadController, mainPanelController, durationFormatter);
    }

    PanelViewHolder(PositionController positionController, MediaViewController mediaController, DownloadController downloadController, MainPanelController mainPanelController, DurationFormatter durationFormatter) {
        this.mediaController = mediaController;
        this.positionController = positionController;
        this.downloadController = downloadController;
        this.mainPanelController = mainPanelController;
    }

    public MediaViewController mediaController() {
        return mediaController;
    }

    public void showCollapsed(boolean isDownloaded) {
        mediaController.showCollapsed(isDownloaded);
        downloadController.panelScopeChange(isDownloaded);
        mainPanelController.makeTopBarSolid();
    }

    public void showExpanded(boolean downloaded) {
        mediaController.showExpanded(downloaded);
        positionController.panelScopeChange(downloaded);
        downloadController.panelScopeChange(downloaded);
        mainPanelController.makeTopBarTransparent();
        mainPanelController.showBottomBar(downloaded);
    }

    public DownloadController downloadController() {
        return downloadController;
    }

    public PositionController positionController() {
        return positionController;
    }

    public void updatePlayingState(boolean playing) {
        if (playing) {
            mediaController.showPause();
        } else {
            mediaController.showPlay();
        }
    }

    public void updatePanel(FullItem fullItem) {
        mainPanelController.updateFrom(fullItem);
        downloadController.panelScopeChange(fullItem.isDownloaded());
        mediaController.setMediaVisibility(mainPanelController.isShowing(), fullItem);
        positionController.setInitialPosition(fullItem.getInitialPlayPosition());
    }

    public void close() {
        mainPanelController.close();
    }

    public void expand() {
        mainPanelController.expand();
    }

    public boolean isShowing() {
        return mainPanelController.isShowing();
    }

    public void setPanelSlideListener(SlidingUpPanelLayout.PanelSlideListener panelSlideListener) {
        mainPanelController.setPanelSlideListener(panelSlideListener);
    }

    public void show() {
        mainPanelController.showPanel();
    }

    public void hide() {
        mainPanelController.hidePanel();
    }

}
