package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.ouchadam.fang.notification.FangNotification;
import com.ouchadam.fang.notification.NotificationService;
import com.ouchadam.fang.notification.PodcastPlayerNotificationEventBroadcaster;
import com.ouchadam.fang.presentation.PlayerEvent;
import com.ouchadam.fang.presentation.PodcastPlayerEventBroadcaster;

public class AudioService extends Service implements ServiceManipulator {

    private final LocalBinder binder;
    private final ServiceLocation serviceLocation;

    private PlayerEventReceiver playerEventReceiver;
    private ExternalReceiver externalReceiver;
    private Syncer syncer;

    public AudioService() {
        binder = new LocalBinder();
        serviceLocation = new ServiceLocation();
    }

    @Override
    public IBinder onBind(Intent intent) {
        dismissNotification();
        serviceLocation.setWithinApp();
        return binder;
    }

    private void dismissNotification() {
        FangNotification.from(this).dismiss();
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
        PlayerHandler playerHandler = PlayerHandler.from(this, onSync, new AudioCompletionHandler(serviceLocation), this);
        this.syncer = new Syncer(playerHandler, serviceLocation);
        initReceivers(playerHandler);
    }

    private final PlayerHandler.AudioSync onSync = new PlayerHandler.AudioSync() {
        @Override
        public void onSync(long itemId, PlayerEvent playerEvent) {
            handleSyncRequest(itemId, playerEvent);
        }
    };

    private void handleSyncRequest(long itemId, PlayerEvent playerEvent) {
        if (serviceLocation.isWithinApp()) {
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
        dismissNotification();
        serviceLocation.setWithinApp();
        syncer.setSyncListener(listener);
        syncForeground();
    }

    private void syncForeground() {
        syncer.sync();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        removeSyncListener();
        serviceLocation.leavingApp();
        showNotification();
        return super.onUnbind(intent);
    }

    private void removeSyncListener() {
        syncer.removeSyncListener();
    }

    private void showNotification() {
        boolean isPlaying = syncer.isPlaying();
        if (!isPlaying) {
            new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().stop());
        }
        if (isPlaying) {
            NotificationService.start(this, syncer.getItemId(), isPlaying);
        }
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
