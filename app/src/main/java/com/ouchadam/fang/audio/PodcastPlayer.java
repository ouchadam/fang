package com.ouchadam.fang.audio;

import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.ouchadam.fang.Broadcaster;

import java.io.IOException;

public class PodcastPlayer {

    private final MediaPlayer mediaPlayer;
    private final Broadcaster<PodcastPosition> positionBroadcaster;

    private boolean isPaused = false;

    private final Handler seekHandler = new Handler();

    public PodcastPlayer(MediaPlayer mediaPlayer, Broadcaster<PodcastPosition> positionBroadcaster) {
        this.mediaPlayer = mediaPlayer;
        this.positionBroadcaster = positionBroadcaster;
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
            positionBroadcaster.broadcast(new PodcastPosition(percent));
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

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }
}
