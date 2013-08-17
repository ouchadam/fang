package com.ouchadam.fang.presentation.controller;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import java.io.IOException;

class PodcastPlayer {

    private final MediaPlayer mediaPlayer;
    private final SlidingPanelViewManipulator viewManipulator;

    private boolean isPaused = false;

    private final Handler seekHandler = new Handler();

    PodcastPlayer(MediaPlayer mediaPlayer, SlidingPanelViewManipulator viewManipulator) {
        this.mediaPlayer = mediaPlayer;
        this.viewManipulator = viewManipulator;
    }

    public void setSource(Context context, Uri source) throws IOException {
        mediaPlayer.setDataSource(context, source);
        mediaPlayer.prepare();
    }

    public void play() {
        isPaused = false;
        mediaPlayer.start();
        scheduleSeekPositionUpdate();
    }

    private final Runnable seekUpdater = new Runnable() {
        @Override
        public void run() {
            float percentCoeff = (float) mediaPlayer.getCurrentPosition() / (float) mediaPlayer.getDuration();
            int percent = (int) (percentCoeff * 100);
            viewManipulator.setSeekProgress(percent);
            if (mediaPlayer.isPlaying()) {
                scheduleSeekPositionUpdate();
            }
        }
    };

    private void scheduleSeekPositionUpdate() {
        seekHandler.postDelayed(seekUpdater, 1000);
    }

    public void pause() {
        isPaused = true;
        mediaPlayer.pause();
    }

    public void goTo(int position) {
        mediaPlayer.seekTo(position);
    }

    public void stop() {
        mediaPlayer.stop();
    }

    public void release() {
        mediaPlayer.release();
    }

    public boolean isPaused() {
        return isPaused;
    }
}
