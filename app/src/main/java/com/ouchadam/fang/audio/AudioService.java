package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ouchadam.fang.presentation.controller.AudioFocusManager;
import com.ouchadam.fang.presentation.controller.FangNotification;

import java.io.IOException;

public class AudioService extends Service implements PlayerEventReceiver.PlayerEventCallbacks {

    private final LocalBinder binder;

    private AudioFocusManager audioFocusManager;
    private PodcastPlayer podcastPlayer;
    private PlayerEventReceiver playerEventReceiver;
    private AudioServiceBinder.OnStateSync listener;
    private FangNotification fangNotification;

    private long playingItemId = -1L;

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
        Log.e("!!!", "Service created");
        audioFocusManager = new AudioFocusManager((AudioManager) getSystemService(Context.AUDIO_SERVICE));
        podcastPlayer = new PodcastPlayer(new PodcastPositionBroadcaster(this));
        playerEventReceiver = new PlayerEventReceiver(this);
        playerEventReceiver.register(this);
        fangNotification = FangNotification.from(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setSyncListener(AudioServiceBinder.OnStateSync listener) {
        this.listener = listener;
        Log.e("!!!", "onBind");
        podcastPlayer.sync(playingItemId, listener);
    }

    @Override
    public void onPlay(PodcastPosition position) {
        play(position);
        Log.e("!!!", "onPlay");
        podcastPlayer.sync(playingItemId, listener);
    }

    private void play(PodcastPosition position) {
        audioFocusManager.requestFocus();
        podcastPlayer.play(position);
    }

    @Override
    public void onPause() {
        pause();
        persistPosition();
        Log.e("!!!", "onPause");
        podcastPlayer.sync(playingItemId, listener);
    }

    private void pause() {
        podcastPlayer.pause();
    }

    @Override
    public void onStop() {
        persistPosition();
        stop();
        stopSelf();
    }

    private void persistPosition() {
        // TODO persist position
    }

    private void stop() {
        audioFocusManager.abandonFocus();
        fangNotification.dismiss();
        podcastPlayer.release();
    }

    @Override
    public void onNewSource(long itemId, Uri source) {
        if (playingItemId != itemId) {
            this.playingItemId = itemId;
            setSource(source);
        }
    }

    @Override
    public void gotoPosition(PodcastPosition position) {
        podcastPlayer.goTo(position.value());
        Log.e("!!!", "onGoTo");
        podcastPlayer.sync(playingItemId, listener);
    }

    private void setSource(Uri uri) {
        try {
            podcastPlayer.setSource(this, uri);
            Log.e("!!!", "onNewSource");
            podcastPlayer.sync(playingItemId, listener);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("couldn't find : " + uri);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.e("!!!", "Service destroyed");
        unregisterEventReciever();
    }

    private void unregisterEventReciever() {
        playerEventReceiver.unregister(this);
    }
}
