package com.ouchadam.fang.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.domain.PodcastPosition;

import java.io.IOException;

public class PodcastPlayer {

    private static final int ONE_SECOND = 1000;
    private final Broadcaster<PodcastPosition> positionBroadcaster;
    private MediaPlayer mediaPlayer;

    private final Handler seekHandler = new Handler();

    public PodcastPlayer(Broadcaster<PodcastPosition> positionBroadcaster) {
        this.positionBroadcaster = positionBroadcaster;
    }

    public void setSource(Context context, Uri source) throws IOException {
        if (mediaPlayer != null) {
            mediaPlayer.reset();
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setDataSource(context, source);
        mediaPlayer.prepare();
    }

    public void play(PodcastPosition position) {
        if (position != null) {
            goTo(position.value());
        }
        mediaPlayer.start();
        scheduleSeekPositionUpdate();
    }

    public void goTo(int position) {
        mediaPlayer.seekTo(position);
    }

    private final Runnable seekUpdater = new Runnable() {
        @Override
        public void run() {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                positionBroadcaster.broadcast(new PodcastPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
                scheduleSeekPositionUpdate();
            }
        }
    };

    private void scheduleSeekPositionUpdate() {
        seekHandler.postDelayed(seekUpdater, ONE_SECOND);
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = null;
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isNotPrepared() {
        return mediaPlayer == null;
    }


    public boolean isPrepared() {
        return mediaPlayer != null;
    }

    public PodcastPosition getPosition() {
        return new PodcastPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
    }

    public void setCompletionListener(MediaPlayer.OnCompletionListener onCompletionListener) {
        mediaPlayer.setOnCompletionListener(onCompletionListener);
    }
}
