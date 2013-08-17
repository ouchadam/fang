package com.ouchadam.fang.presentation.controller;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class SlidingPanelViewManipulator {

    private final SlidingUpPanelLayout panelLayout;
    private final SeekBar seekBar;
    private final ViewSwitcher viewSwitcher;

    public enum MediaPressed {
        PLAY,
        PAUSE;
    }

    public interface OnMediaClickListener {
        void onMediaClicked(MediaPressed mediaPressed);
    }

    public interface OnPanelChangeListener {
        void onPanelExpanded(View panel);
        void onPanelCollapsed(View panel);
    }

    private OnMediaClickListener mediaClickedListener;

    SlidingPanelViewManipulator(SlidingUpPanelLayout panelLayout, SeekBar seekBar, ViewSwitcher viewSwitcher) {
        this.panelLayout = panelLayout;
        this.seekBar = seekBar;
        this.viewSwitcher = viewSwitcher;
    }

    public void setOnPanelExpandListener(final OnPanelChangeListener onPanelExpandListener) {
        panelLayout.setPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                // ignore
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

    public void fromItem(Item item) {
        setBarText(item.getTitle());
        setDescription(item.getSummary());
    }

    private void setBarText(CharSequence text) {
        setTextViewText(text, R.id.bar_title);
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
