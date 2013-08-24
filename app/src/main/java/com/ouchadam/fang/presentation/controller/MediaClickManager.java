package com.ouchadam.fang.presentation.controller;

import android.view.View;

public class MediaClickManager {

    public enum MediaPressed {
        PLAY,
        PAUSE;
    }

    public interface OnMediaClickListener {
        void onMediaClicked(MediaPressed mediaPressed);
    }

    private final OnMediaClickListener listener;
    private final MediaViewController mediaController;

    public MediaClickManager(OnMediaClickListener listener, MediaViewController mediaController) {
        this.listener = listener;
        this.mediaController = mediaController;
        mediaController.setPlayPauseListeners(onPlayClicked, onPauseClicked);
    }

    private final View.OnClickListener onPlayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.PLAY);
            mediaNext();
        }
    };

    private void mediaNext() {
        mediaController.showNext();
    }

    private final View.OnClickListener onPauseClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.PAUSE);
            mediaNext();
        }
    };

}
