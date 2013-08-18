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

class SlidingPanelViewManipulator {

    private final ActionBarManipulator actionBarManipulator;
    private final SlidingUpPanelLayout panelLayout;
    private final SeekBar seekBar;
    private final ViewSwitcher viewSwitcher;

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

    public interface OnPanelChangeListener {
        void onPanelExpanded(View panel);

        void onPanelCollapsed(View panel);
    }

    private OnMediaClickListener mediaClickedListener;

    SlidingPanelViewManipulator(ActionBarManipulator actionBarManipulator, SlidingUpPanelLayout panelLayout, SeekBar seekBar, ViewSwitcher viewSwitcher) {
        this.actionBarManipulator = actionBarManipulator;
        this.panelLayout = panelLayout;
        this.seekBar = seekBar;
        this.viewSwitcher = viewSwitcher;
    }

    public interface ActionBarManipulator {
        boolean isActionBarShowing();

        void hideActionBar();

        void showActionBar();
    }

    public void setOnPanelExpandListener(final OnPanelChangeListener onPanelExpandListener) {
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
        panelLayout.findViewById(R.id.top_bar_play).setOnClickListener(onPlayClicked);
        panelLayout.findViewById(R.id.top_bar_pause).setOnClickListener(onPauseClicked);
    }

    private final View.OnClickListener onPlayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaClickedListener.onMediaClicked(MediaPressed.PLAY);
            viewSwitcher.showNext();
        }
    };

    private final View.OnClickListener onPauseClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mediaClickedListener.onMediaClicked(MediaPressed.PAUSE);
            viewSwitcher.showNext();
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

}
