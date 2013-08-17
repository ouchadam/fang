package com.ouchadam.fang.presentation.controller;

import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;
import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class SlidingPanelViewManipulator {

    private final SlidingUpPanelLayout panelLayout;
    private final SeekBar seekBar;
    private final ViewSwitcher viewSwitcher;


    public enum MediaPressed {
        PLAY,
        PAUSE;
    }

    public interface OnMediaClickedListener {
        void onMediaClicked(MediaPressed mediaPressed);
    }

    private OnMediaClickedListener mediaClickedListener;

    SlidingPanelViewManipulator(SlidingUpPanelLayout panelLayout, SeekBar seekBar, ViewSwitcher viewSwitcher) {
        this.panelLayout = panelLayout;
        this.seekBar = seekBar;
        this.viewSwitcher = viewSwitcher;
    }

    public void setMediaClickedListener(OnMediaClickedListener mediaClickedListener) {
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

    public void setBarText(CharSequence text) {
        TextView barTitle = Views.findById(panelLayout, R.id.bar_title);
        barTitle.setText(text);
    }

    public void setSeekProgress(int percent) {
        seekBar.setProgress(percent);
    }

}
