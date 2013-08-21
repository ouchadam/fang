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

    public void play(PodcastPosition position) {
        isPaused = false;
        goTo(position.value());
        mediaPlayer.start();
        scheduleSeekPositionUpdate();
    }

    private void goTo(int position) {
        mediaPlayer.seekTo(position);
    }

    private final Runnable seekUpdater = new Runnable() {
        @Override
        public void run() {
            positionBroadcaster.broadcast(new PodcastPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
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

    public void sync(AudioServiceBinder.OnStateSync listener) {
        if (mediaPlayer.getDuration() == 0 && mediaPlayer.getCurrentPosition() == 0) {
            listener.onSync(mediaPlayer.isPlaying(), new PodcastPosition(0, 1));
        } else {
            listener.onSync(mediaPlayer.isPlaying(), new PodcastPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration()));
        }
    }
}
