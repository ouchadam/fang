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
    private final OverflowController overflowController;

    public static PanelViewHolder from(SlidingUpPanelLayout panel, OverflowCallback overflowCallback) {
        ViewSwitcher topMediaSwitcher = Views.findById(panel, R.id.media_switcher);
        ViewSwitcher bottomMediaSwitcher = Views.findById(panel, R.id.bottom_media_switcher);

        View playTop = Views.findById(panel, R.id.play_top);
        View playBottom = Views.findById(panel, R.id.play_bottom);
        View pauseTop = Views.findById(panel, R.id.pause_top);
        View pauseBottom = Views.findById(panel, R.id.pause_bottom);
        View rewind = Views.findById(panel, R.id.rewind);
        View fastForward = Views.findById(panel, R.id.fast_forward);
        View next = Views.findById(panel, R.id.next);
        View previous = Views.findById(panel, R.id.previous);

        MediaViewController mediaController = new MediaViewController(playTop, playBottom, pauseTop, pauseBottom, rewind, fastForward, next, previous, topMediaSwitcher, bottomMediaSwitcher);

        ImageButton downloadButton = Views.findById(panel, R.id.download);
        DownloadController downloadController = new DownloadController(downloadButton);

        SeekBar seekBar = Views.findById(panel, R.id.seek_bar);
        TextView currentTime = Views.findById(panel, R.id.current_position);
        TextView endTime = Views.findById(panel, R.id.total_time);
        PositionController position = new PositionController(seekBar, currentTime, endTime);


        ImageView heroView = Views.findById(panel, R.id.drawer_content_image);

        HeroManager heroManager = new HeroManager(new HeroHolder(), heroView, panel.getContext());
        DurationFormatter durationFormatter = new DurationFormatter(panel.getResources());
        MainPanelController mainPanelController = new MainPanelController(panel, durationFormatter, heroManager);

        ImageButton overflowButton = Views.findById(panel, R.id.drawer_overflow);
        OverflowController overflowController = new OverflowController(overflowButton, overflowCallback);

        return new PanelViewHolder(position, mediaController, downloadController, mainPanelController, overflowController);
    }

    PanelViewHolder(PositionController positionController, MediaViewController mediaController, DownloadController downloadController, MainPanelController mainPanelController, OverflowController overflowController) {
        this.mediaController = mediaController;
        this.positionController = positionController;
        this.downloadController = downloadController;
        this.mainPanelController = mainPanelController;
        this.overflowController = overflowController;
    }

    public MediaViewController mediaController() {
        return mediaController;
    }

    public void showCollapsed(boolean isDownloaded) {
        mediaController.showCollapsed(isDownloaded);
        downloadController.showCollapsed(isDownloaded);
        mainPanelController.makeTopBarSolid();
        overflowController.showCollapsed(isDownloaded);
    }

    public void showExpanded(boolean downloaded) {
        mediaController.showExpanded(downloaded);
        positionController.panelScopeChange(downloaded);
        downloadController.showExpanded(downloaded);
        mainPanelController.makeTopBarTransparent();
        mainPanelController.showBottomBar(downloaded);
        overflowController.showExpanded(downloaded);
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
        downloadController.update(fullItem.isDownloaded());
        mediaController.setMediaVisibility(isExpanded(), fullItem);
        positionController.setInitialPosition(fullItem.getInitialPlayPosition());
        overflowController.initOverflow(fullItem);
    }

    public void close() {
        mainPanelController.close();
    }

    public void expand() {
        mainPanelController.expand();
    }

    public boolean isExpanded() {
        return mainPanelController.isExpanded();
    }

    public void setPanelSlideListener(SlidingUpPanelLayout.PanelSlideListener panelSlideListener) {
        mainPanelController.setPanelSlideListener(panelSlideListener);
    }

    public void show() {
        mainPanelController.showPanel();
    }

    public void hide() {
        showCollapsed(true);
        mainPanelController.hidePanel();
    }

}
