package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.PositionPersister;
import com.ouchadam.fang.presentation.controller.AudioFocusManager;
import com.ouchadam.fang.notification.FangNotification;
import com.ouchadam.fang.presentation.controller.PlayerEvent;
import com.ouchadam.fang.notification.PodcastPlayerNotificationEventBroadcaster;

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
    private ExternalReceiver externalReceiver;

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
        initReceivers();
        fangNotification = FangNotification.from(this);
    }

    private void initReceivers() {
        playerEventReceiver = new PlayerEventReceiver(this);
        playerEventReceiver.register(this);
        externalReceiver = new ExternalReceiver();
        externalReceiver.register(this);
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
    public void onNewSource(long itemId, Uri source) {
        this.playingItemId = itemId;
        setSource(source);
        podcastPlayer.setCompletionListener(onComplete);
        sync(new PlayerEvent.Factory().newSource(itemId, source));
    }

    private void sync(PlayerEvent playerEvent) {
        if (isWithinApp()) {
            sync();
        } else {
            Log.e("!!!!", "Sending event : " + playerEvent.getEvent().name() + " to notification with ID : " + playingItemId);
            new PodcastPlayerNotificationEventBroadcaster(playingItemId, this).broadcast(playerEvent);
        }
    }

    private void setSource(Uri uri) {
        try {
            podcastPlayer.setSource(this, uri);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("couldn't find : " + uri);
        }
    }

    private final MediaPlayer.OnCompletionListener onComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (hasNext()) {
                // TODO implement a queue
            } else {
                if (isWithinApp()) {
                    onPause();
                } else {
                    onStop();
                }
            }
        }
    };

    private boolean hasNext() {
        return false;
    }

    @Override
    public void onPlay(PodcastPosition position) {
        play(position);
        sync(new PlayerEvent.Factory().play(position));
    }

    private void play(PodcastPosition position) {
        audioFocusManager.requestFocus();
        podcastPlayer.play(position);
    }

    @Override
    public void onPause() {
        pause();
        persistCurrentId();
        persistPosition();
        sync(new PlayerEvent.Factory().pause());
    }

    private void pause() {
        podcastPlayer.pause();
        audioFocusManager.abandonFocus();
    }

    @Override
    public void onStop() {
        if (podcastPlayer.isPrepared()) {
            persistCurrentId();
            persistPosition();
        }
        stop();
        stopSelf();
    }

    private void persistPosition() {
        if (playingItemId != MISSING_ID) {
            new PositionPersister(playingItemId, getContentResolver()).persist(podcastPlayer.getPosition());
        }
    }

    private void persistCurrentId() {
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
    public void gotoPosition(PodcastPosition position) {
        podcastPlayer.goTo(position.value());
        sync();
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

    private boolean isWithinApp() {
        return listener != null;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        listener = null;
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceivers();
    }

    private void unregisterReceivers() {
        playerEventReceiver.unregister(this);
        externalReceiver.unregister(this);
    }
}
