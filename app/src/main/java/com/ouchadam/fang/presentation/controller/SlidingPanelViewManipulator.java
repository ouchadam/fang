package com.ouchadam.fang.presentation.controller;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class SlidingPanelViewManipulator implements OnPanelChangeListener {

    private final ActionBarManipulator actionBarManipulator;
    private final SlidingUpPanelLayout panelLayout;
    private final SeekBar seekBar;
    private final ViewSwitcher topMediaSwitcher;
    private final ViewSwitcher bottomMediaSwitcher;

    private FullItem fullItem;

    public enum MediaPressed {
        PLAY,
        PAUSE;
    }

    public interface OnMediaClickListener {
        void onMediaClicked(MediaPressed mediaPressed);
    }

    public interface OnDownloadClickListener {
        void onDownloadClicked(FullItem fullItem);
    }

    private OnMediaClickListener mediaClickedListener;

    public static SlidingPanelViewManipulator from(ActionBarManipulator actionBarManipulator, View root) {
        SlidingUpPanelLayout slidingPanel = Views.findById(root, R.id.sliding_layout);
        slidingPanel.setShadowDrawable(root.getResources().getDrawable(R.drawable.above_shadow));
        return new SlidingPanelViewManipulator(actionBarManipulator, slidingPanel);
    }

    SlidingPanelViewManipulator(ActionBarManipulator actionBarManipulator, SlidingUpPanelLayout panelLayout) {
        this.actionBarManipulator = actionBarManipulator;
        this.panelLayout = panelLayout;
        this.seekBar = Views.findById(panelLayout, R.id.seek_bar);
        this.topMediaSwitcher = Views.findById(panelLayout, R.id.media_switcher);
        this.bottomMediaSwitcher = Views.findById(panelLayout, R.id.bottom_media_switcher);
        setOnPanelExpandListener(this);
    }

    public interface ActionBarManipulator {
        boolean isActionBarShowing();

        void hideActionBar();

        void showActionBar();
    }

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
        Views.findById(panelLayout, R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDownloadClickedListener.onDownloadClicked(fullItem);
            }
        });
    }

    public void setMediaClickedListener(OnMediaClickListener mediaClickedListener) {
        this.mediaClickedListener = mediaClickedListener;
        panelLayout.findViewById(R.id.play_top).setOnClickListener(onPlayClicked);
        panelLayout.findViewById(R.id.play_bottom).setOnClickListener(onPlayClicked);
        panelLayout.findViewById(R.id.pause_top).setOnClickListener(onPauseClicked);
        panelLayout.findViewById(R.id.pause_bottom).setOnClickListener(onPauseClicked);
    }

    private final View.OnClickListener onPlayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaClickedListener.onMediaClicked(MediaPressed.PLAY);
            mediaNext();
        }
    };

    private void mediaNext() {
        topMediaSwitcher.showNext();
        bottomMediaSwitcher.showNext();
    }

    private final View.OnClickListener onPauseClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaClickedListener.onMediaClicked(MediaPressed.PAUSE);
            mediaNext();
        }
    };

    public void setSeekProgress(int percent) {
        seekBar.setProgress(percent);
    }

    public void expand() {
        panelLayout.expandPane();
    }

    public void fromItem(FullItem fullItem) {
        this.fullItem = fullItem;
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
        syncDownloadState();
        showExpanded();
    }

    private void syncDownloadState() {
        if (isDownloaded()) {
            Views.findById(panelLayout, R.id.download).setVisibility(View.INVISIBLE);
        } else {
            Views.findById(panelLayout, R.id.download).setVisibility(View.VISIBLE);
        }
    }

    private void showExpanded() {
        topMediaSwitcher.setVisibility(View.INVISIBLE);
        if (isDownloaded()) {
            bottomMediaSwitcher.setVisibility(View.VISIBLE);
        } else {
            bottomMediaSwitcher.setVisibility(View.INVISIBLE);
        }
    }

    private boolean isDownloaded() {
        return fullItem != null && fullItem.isDownloaded();
    }

    @Override
    public void onPanelCollapsed(View panel) {
        syncDownloadState();
        showCollapsed();
    }

    private void showCollapsed() {
        if (isDownloaded()) {
            topMediaSwitcher.setVisibility(View.VISIBLE);
        } else {
            topMediaSwitcher.setVisibility(View.INVISIBLE);
        }
    }

}
