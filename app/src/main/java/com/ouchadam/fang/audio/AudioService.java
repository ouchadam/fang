package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;

import com.ouchadam.fang.notification.PodcastPlayerNotificationEventBroadcaster;
import com.ouchadam.fang.presentation.controller.PlayerEvent;

public class AudioService extends Service implements ServiceManipulator {

    private final LocalBinder binder;

    private PlayerEventReceiver playerEventReceiver;
    private ExternalReceiver externalReceiver;
    private PlayerHandler playerHandler;
    private Syncer syncer;

    public AudioService() {
        binder = new LocalBinder();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void stop() {
        stopSelf();
    }

    public class LocalBinder extends Binder {
        public AudioService getService() {
            return AudioService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        playerHandler = PlayerHandler.from(this, onSync, onComplete, this);
        this.syncer = new Syncer(playerHandler);
        initReceivers(playerHandler);
    }

    private final PlayerHandler.AudioSync onSync = new PlayerHandler.AudioSync() {
        @Override
        public void onSync(long itemId, PlayerEvent playerEvent) {
            handleSyncRequest(itemId, playerEvent);
        }
    };

    private void handleSyncRequest(long itemId, PlayerEvent playerEvent) {
        if (syncer.isWithinApp()) {
            syncForeground();
        } else {
            broadcastToNotification(itemId, playerEvent);
        }
    }

    private void broadcastToNotification(long itemId, PlayerEvent playerEvent) {
        new PodcastPlayerNotificationEventBroadcaster(itemId, this).broadcast(playerEvent);
    }

    private void initReceivers(PlayerHandler playerHandler) {
        playerEventReceiver = new PlayerEventReceiver(playerHandler);
        playerEventReceiver.register(this);
        externalReceiver = new ExternalReceiver();
        externalReceiver.register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void setSyncListener(AudioServiceBinder.OnStateSync listener) {
        syncer.setSyncListener(listener);
        syncForeground();
    }

    private void syncForeground() {
        syncer.sync();
    }

    private final MediaPlayer.OnCompletionListener onComplete = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            if (syncer.isWithinApp()) {
                playerHandler.onPause();
            } else {
                playerHandler.onStop();
            }
        }
    };

    @Override
    public boolean onUnbind(Intent intent) {
        removeSyncListener();
        return super.onUnbind(intent);
    }

    private void removeSyncListener() {
        syncer.removeSyncListener();
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
