package com.ouchadam.fang.presentation.panel;

import android.view.View;

public class MediaClickManager {

    public enum MediaPressed {
        PLAY,
        PAUSE,
        REWIND,
        FAST_FORWARD,
        NEXT,
        PREVIOUS;
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
        mediaController.setRewindFastForwardListeners(onRewindClicked, onFastForwardClicked);
        mediaController.setNextPreviousListeners(onNextClicked, onPreviousClicked);
    }

    private final View.OnClickListener onPlayClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.PLAY);
            mediaNext();
        }
    };

    private final View.OnClickListener onPauseClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.PAUSE);
            mediaNext();
        }
    };

    private void mediaNext() {
        mediaController.showNext();
    }


    private final View.OnClickListener onRewindClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.REWIND);
        }
    };


    private final View.OnClickListener onFastForwardClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.FAST_FORWARD);
        }
    };

    private final View.OnClickListener onNextClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.NEXT);
        }
    };


    private final View.OnClickListener onPreviousClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            listener.onMediaClicked(MediaPressed.PREVIOUS);
        }
    };

}
