package com.ouchadam.fang.audio;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.domain.PodcastPosition;

import java.io.IOException;

public class FangMediaPlayer implements FangPlayer {

    private static final int ONE_SECOND_MS = 1000;

    private final Handler seekHandler = new Handler();
    private final Context context;
    private final Broadcaster<PodcastPosition> positionBroadcaster;
    private final OnBadSourceHandler onBadSourceHandler;
    private final PodcastPlayerEventBroadcaster playerEventBroadcaster;

    private MediaPlayer mediaPlayer;
    private Uri source;

    public interface OnBadSourceHandler {
        void onBadSource(Exception e);
    }

    public static FangPlayer from(Context context, OnBadSourceHandler onBadSourceHandler) {
        return new FangMediaPlayer(context, new PodcastPositionBroadcaster(context), onBadSourceHandler, new PodcastPlayerEventBroadcaster(context));
    }

    public FangMediaPlayer(Context context, Broadcaster<PodcastPosition> positionBroadcaster, OnBadSourceHandler onBadSourceHandler, PodcastPlayerEventBroadcaster playerEventBroadcaster) {
        this.context = context;
        this.positionBroadcaster = positionBroadcaster;
        this.onBadSourceHandler = onBadSourceHandler;
        this.playerEventBroadcaster = playerEventBroadcaster;
        this.source = null;
    }

    @Override
    public void setSource(Uri source) {
        reset();
        this.source = source;
        this.mediaPlayer = getMediaPlayerWithSource(source);
        if (mediaPlayer != null) {
            initMediaPlayer(mediaPlayer);
        }
    }

    private MediaPlayer getMediaPlayerWithSource(Uri source) {
        MediaPlayer mediaPlayer = getMediaPlayer();
        try {
            mediaPlayer.setDataSource(context, source);
            mediaPlayer.prepare();
            return mediaPlayer;
        } catch (IOException e) {
            onBadSourceHandler.onBadSource(e);
            return null;
        }
    }

    private MediaPlayer getMediaPlayer() {
        return mediaPlayer == null ? new MediaPlayer() : mediaPlayer;
    }

    private void reset() {
        if (mediaPlayer != null) {
            this.mediaPlayer.reset();
        }
        this.source = null;
    }

    private void initMediaPlayer(MediaPlayer mediaPlayer) {
        mediaPlayer.setOnCompletionListener(onCompletionWrapper);
    }

    private final MediaPlayer.OnCompletionListener onCompletionWrapper = new MediaPlayer.OnCompletionListener() {

        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            playerEventBroadcaster.broadcast(new PlayerEvent.Factory().complete());
        }
    };

    @Override
    public void play(PodcastPosition position) {
        setInitialPlayPosition(position);
        mediaPlayer.start();
        scheduleSeekPositionUpdate();
    }

    private void setInitialPlayPosition(PodcastPosition position) {
        if (hasNonCompletedPosition(position)) {
            goTo(position);
        }
    }

    private boolean hasNonCompletedPosition(PodcastPosition position) {
        return position != null && !position.isCompleted();
    }

    @Override
    public void goTo(PodcastPosition podcastPosition) {
        mediaPlayer.seekTo(podcastPosition.value());
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
        seekHandler.postDelayed(seekUpdater, ONE_SECOND_MS);
    }

    @Override
    public void pause() {
        mediaPlayer.pause();
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public boolean isPrepared() {
        return mediaPlayer != null;
    }

    public PodcastPosition getPosition() {
        return new PodcastPosition(mediaPlayer.getCurrentPosition(), mediaPlayer.getDuration());
    }

    @Override
    public Uri getSource() {
        return source;
    }

    @Override
    public void release() {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        this.source = null;
        this.mediaPlayer = null;
    }

}
