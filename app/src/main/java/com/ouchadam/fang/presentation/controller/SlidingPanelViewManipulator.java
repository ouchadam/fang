package com.ouchadam.fang.presentation.controller;

import android.view.View;
import android.widget.TextView;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.PodcastPosition;
import com.ouchadam.fang.audio.SeekbarReceiver;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class SlidingPanelViewManipulator implements OnPanelChangeListener {

    private final ActionBarManipulator actionBarManipulator;
    private final SlidingUpPanelLayout panelLayout;
    private final PanelViewHolder panelViewHolder;
    private final DownloadFoo downloadFoo;
    private final PositionManager positionManager;

    public void setPlayingState(boolean playing) {
        if (playing) {
            if (panelViewHolder.isInPlayMode()) {
                panelViewHolder.mediaController().showNext();
            }
        }
    }

    public void update(PodcastPosition position) {
        positionManager.update(position);
    }

    public PodcastPosition getPosition() {
        return positionManager.getLatestPosition();
    }

    public void setMediaClickedListener(MediaClickManager.OnMediaClickListener onMediaClickListener) {
        MediaClickManager mediaClickManager = new MediaClickManager(onMediaClickListener, panelViewHolder.mediaController());
    }

    public interface OnDownloadClickListener {
        void onDownloadClicked(FullItem fullItem);
    }

    public interface OnSeekChanged {
        void onSeekChanged(PodcastPosition position);
    }

    public static SlidingPanelViewManipulator from(ActionBarManipulator actionBarManipulator, OnSeekChanged onSeekChanged, View root) {
        SlidingUpPanelLayout slidingPanel = Views.findById(root, R.id.sliding_layout);
        slidingPanel.setShadowDrawable(root.getResources().getDrawable(R.drawable.above_shadow));
        return new SlidingPanelViewManipulator(actionBarManipulator, onSeekChanged, slidingPanel);
    }

    SlidingPanelViewManipulator(ActionBarManipulator actionBarManipulator, OnSeekChanged onSeekChanged, SlidingUpPanelLayout panelLayout) {
        this.actionBarManipulator = actionBarManipulator;
        this.panelLayout = panelLayout;
        this.panelViewHolder = PanelViewHolder.from(panelLayout);

        this.downloadFoo = new DownloadFoo(panelViewHolder.downloadController());

        positionManager = new PositionManager(onSeekChanged, new SeekbarReceiver(seekUpdate), panelViewHolder.positionController());

        setOnPanelExpandListener(this);
    }

    private final SeekbarReceiver.OnSeekUpdate seekUpdate = new SeekbarReceiver.OnSeekUpdate() {
        @Override
        public void onUpdate(PodcastPosition position) {
            positionManager.update(position);
        }
    };

    private void setOnPanelExpandListener(final OnPanelChangeListener onPanelExpandListener) {
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                if (slideOffset < 0.2) {
                    if (actionBarManipulator.isActionBarShowing()) {
                        actionBarManipulator.hideActionBar();
                    }
                } else {
                    if (!actionBarManipulator.isActionBarShowing()) {
                        actionBarManipulator.showActionBar();
                    }
                }
            }

            @Override
            public void onPanelCollapsed(View panel) {
                onPanelExpandListener.onPanelCollapsed(panel);
            }

            @Override
            public void onPanelExpanded(View panel) {
                onPanelExpandListener.onPanelExpanded(panel);
            }
        });
    }

    public void setOnDownloadClickedListener(final OnDownloadClickListener onDownloadClickedListener) {
        downloadFoo.setListener(onDownloadClickedListener);
    }

    public void expand() {
        panelLayout.expandPane();
    }

    public void fromItem(FullItem fullItem) {
        downloadFoo.itemChange(fullItem);

        Item item = fullItem.getItem();
        setBarTitle(item.getTitle());
        setDescription(item.getSummary());
        setBarSubtitle(fullItem.getChannelTitle());
        setMediaVisibility(fullItem);
    }

    private void setMediaVisibility(FullItem fullItem) {
        Views.findById(panelLayout, R.id.media_switcher).setVisibility(fullItem.isDownloaded() ? View.VISIBLE : View.INVISIBLE);
    }

    private void setBarTitle(CharSequence text) {
        setTextViewText(text, R.id.bar_title);
    }

    private void setBarSubtitle(CharSequence text) {
        setTextViewText(text, R.id.bar_sub_title);
    }

    private void setDescription(CharSequence summary) {
        setTextViewText(summary, R.id.item_description);
    }

    private void setTextViewText(CharSequence text, int viewId) {
        TextView textView = Views.findById(panelLayout, viewId);
        textView.setText(text);
    }

    public void close() {
        panelLayout.collapsePane();
    }

    public boolean isShowing() {
        return panelLayout.isExpanded();
    }

    @Override
    public void onPanelExpanded(View panel) {
        showExpanded(downloadFoo.isDownloaded());
        positionManager.registerForUpdates(panel.getContext());
    }

    private void showExpanded(boolean isDownloaded) {
        panelViewHolder.showExpanded(isDownloaded);
    }

    @Override
    public void onPanelCollapsed(View panel) {
        showCollapsed(downloadFoo.isDownloaded());
        positionManager.unregisterForUpdates(panel.getContext());
    }


    private void showCollapsed(boolean isDownloaded) {
        panelViewHolder.showCollapsed(isDownloaded);
    }

}
