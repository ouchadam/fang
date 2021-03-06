package com.ouchadam.fang.presentation.panel;

import android.view.View;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.SeekbarReceiver;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.drawer.DrawerDisEnabler;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

public class SlidingPanelViewManipulator implements OnPanelChangeListener {

    private final ActionBarManipulator actionBarManipulator;
    private final PanelViewHolder panelViewHolder;
    private final DownloadClickAttacher downloadClickAttacher;
    private final PositionManager positionManager;
    private final DrawerDisEnabler drawerDisEnabler;
    private boolean isPlaying;

    public interface OnDownloadClickListener {
        void onDownloadClicked(FullItem fullItem);
    }

    public interface OnSeekChanged {
        void onSeekChanged(PodcastPosition position);
    }

    public static SlidingPanelViewManipulator from(ActionBarManipulator actionBarManipulator, OnSeekChanged onSeekChanged, View root, DrawerDisEnabler drawerDisEnabler, OnDownloadClickListener onDownload, OverflowCallback overflowCallback) {
        SlidingUpPanelLayout slidingPanel = Views.findById(root, R.id.sliding_layout);
        slidingPanel.setShadowDrawable(root.getResources().getDrawable(R.drawable.above_shadow));
        PanelViewHolder panelViewHolder = PanelViewHolder.from(slidingPanel, overflowCallback);
        return new SlidingPanelViewManipulator(actionBarManipulator, onSeekChanged, panelViewHolder, onDownload, drawerDisEnabler);
    }

    SlidingPanelViewManipulator(ActionBarManipulator actionBarManipulator, OnSeekChanged onSeekChanged, PanelViewHolder panelViewHolder, OnDownloadClickListener onDownload, DrawerDisEnabler drawerDisEnabler) {
        this.actionBarManipulator = actionBarManipulator;
        this.panelViewHolder = panelViewHolder;
        this.drawerDisEnabler = drawerDisEnabler;
        this.downloadClickAttacher = new DownloadClickAttacher(panelViewHolder.downloadController(), onDownload);
        positionManager = new PositionManager(onSeekChanged, new SeekbarReceiver(seekUpdate), panelViewHolder.positionController());
        this.isPlaying = false;

        setOnPanelExpandListener(this);
    }

    private final SeekbarReceiver.OnSeekUpdate seekUpdate = new SeekbarReceiver.OnSeekUpdate() {
        @Override
        public void onUpdate(PodcastPosition position) {
            positionManager.update(position);
        }
    };

    private void setOnPanelExpandListener(final OnPanelChangeListener onPanelChangeListener) {
        panelViewHolder.setPanelSlideListener(new PanelChangeHandler(actionBarManipulator, onPanelChangeListener));
    }

    public void setPlayingState(boolean playing) {
        this.isPlaying = playing;
        panelViewHolder.updatePlayingState(playing);
        positionManager.upatePlayingState(playing);
    }

    public void update(PodcastPosition position) {
        positionManager.update(position);
    }

    public PodcastPosition getPosition() {
        return positionManager.getLatestPosition();
    }

    public void setMediaClickedListener(MediaClickManager.OnMediaClickListener onMediaClickListener) {
        // TODO : not a fan of this
        MediaClickManager mediaClickManager = new MediaClickManager(onMediaClickListener, panelViewHolder.mediaController());
    }

    public void show() {
        panelViewHolder.show();
    }


    public void hide() {
        panelViewHolder.hide();
    }

    public boolean isPlaying() {
        return this.isPlaying;
    }

    public void expand() {
        showExpanded(downloadClickAttacher.isDownloaded());
        panelViewHolder.expand();
    }

    public void fromItem(FullItem fullItem) {
        downloadClickAttacher.itemChange(fullItem);
        panelViewHolder.updatePanel(fullItem);
    }

    public void close() {
        panelViewHolder.close();
    }

    public boolean isShowing() {
        return panelViewHolder.isExpanded();
    }

    @Override
    public void onPanelExpanded(View panel) {
        if (drawerDisEnabler != null) {
            drawerDisEnabler.disable();
        }
        showExpanded(downloadClickAttacher.isDownloaded());
        positionManager.registerForUpdates(panel.getContext());
    }

    private void showExpanded(boolean isDownloaded) {
        panelViewHolder.showExpanded(isDownloaded);
    }

    @Override
    public void onPanelCollapsed(View panel) {
        if (drawerDisEnabler != null) {
            drawerDisEnabler.enable();
        }
        showCollapsed(downloadClickAttacher.isDownloaded());
        positionManager.unregisterForUpdates(panel.getContext());
    }

    private void showCollapsed(boolean isDownloaded) {
        panelViewHolder.showCollapsed(isDownloaded);
    }

}
