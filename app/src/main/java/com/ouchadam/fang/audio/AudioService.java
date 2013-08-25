package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.PositionPersister;
import com.ouchadam.fang.presentation.controller.AudioFocusManager;
import com.ouchadam.fang.presentation.controller.FangNotification;

import java.io.IOException;

public class AudioService extends Service implements PlayerEventReceiver.PlayerEventCallbacks {

    private static final long MISSING_ID = -1L;
    private final LocalBinder binder;

    private AudioFocusManager audioFocusManager;
    private PodcastPlayer podcastPlayer;
    private PlayerEventReceiver playerEventReceiver;
    private AudioServiceBinder.OnStateSync listener;
    private FangNotification fangNotification;

    private long playingItemId = MISSING_ID;

    public AudioService() {
        binder = new LocalBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.playingItemId = getStoredPlayingId();
        audioFocusManager = new AudioFocusManager((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        podcastPlayer = new PodcastPlayer(new PodcastPositionBroadcaster(this));
        playerEventReceiver = new PlayerEventReceiver(this);
        playerEventReceiver.register(this);
        fangNotification = FangNotification.from(this);
    }

    private long getStoredPlayingId() {
        return getSharedPreferences(AudioService.class.getSimpleName(), Context.MODE_PRIVATE).getLong("id", MISSING_ID);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setSyncListener(AudioServiceBinder.OnStateSync listener) {
        this.listener = listener;
        sync();
    }

    @Override
    public void onPlay(PodcastPosition position) {
        play(position);
        sync();
    }

    private void play(PodcastPosition position) {
        audioFocusManager.requestFocus();
        podcastPlayer.play(position);
    }

    @Override
    public void onPause() {
        pause();
        persistCurrentId();
        sync();
    }

    private void pause() {
        podcastPlayer.pause();
    }

    @Override
    public void onStop() {
        persistCurrentId();
        persistPosition();
        stop();
        stopSelf();
    }

    private void persistPosition() {
        if (playingItemId != MISSING_ID) {
            new PositionPersister(playingItemId, getContentResolver()).persist(podcastPlayer.getPosition());
        }
    }

    private void persistCurrentId() {
        // TODO persist position
        if (playingItemId != MISSING_ID) {
            SharedPreferences preferences = getSharedPreferences(AudioService.class.getSimpleName(), Context.MODE_PRIVATE);
            preferences.edit().putLong("id", playingItemId).commit();
        }
    }

    private void stop() {
        audioFocusManager.abandonFocus();
        fangNotification.dismiss();
        podcastPlayer.release();
    }

    @Override
    public void onNewSource(long itemId, Uri source) {
        if (podcastPlayer.isNotPrepared()) {
            this.playingItemId = itemId;
            setSource(source);
        }
    }

    @Override
    public void gotoPosition(PodcastPosition position) {
        podcastPlayer.goTo(position.value());
        sync();
    }

    private void setSource(Uri uri) {
        try {
            podcastPlayer.setSource(this, uri);
            sync();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("couldn't find : " + uri);
        }
    }

    private void sync() {
        SyncEvent syncEvent;
        if (playingItemId == MISSING_ID) {
            syncEvent = SyncEvent.fresh();
        } else {
            if (podcastPlayer.isNotPrepared()) {
                syncEvent = SyncEvent.idle(playingItemId);
            } else {
                syncEvent = new SyncEvent(podcastPlayer.isPlaying(), podcastPlayer.getPosition(), playingItemId);
            }
        }
        listener.onSync(syncEvent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterEventReceiver();
    }

    private void unregisterEventReceiver() {
        playerEventReceiver.unregister(this);
    }
}
