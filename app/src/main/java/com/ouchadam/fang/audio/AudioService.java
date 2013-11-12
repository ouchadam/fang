package com.ouchadam.fang.audio;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Binder;
import android.os.IBinder;

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

    private boolean configChanged;
    private AudioCompletionHandler audioCompletionHandler;

    public AudioService() {
        this.binder = new LocalBinder();
        this.serviceLocation = new ServiceLocation();
        this.configChanged = false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public void fangBind() {
        configChanged = false;
        serviceLocation.binding();
        dismissNotification();
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
        audioCompletionHandler = new AudioCompletionHandler(serviceLocation);
        PlayerHandler playerHandler = PlayerHandler.from(this, onSync, audioCompletionHandler, this);
        this.syncer = new Syncer(playerHandler);
        playerHandler.restoreItem();
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

    public void setCompletionListener(CompletionListener onCompletionListener) {
        audioCompletionHandler.setActivityListener(onCompletionListener);
    }

    public void setSyncListener(OnStateSync listener) {
        syncer.setSyncListener(listener);
        syncForeground();
    }

    private void syncForeground() {
        syncer.sync();
    }

    public void fangUnbind() {
        serviceLocation.unbinding();
        if (!serviceLocation.isWithinApp() && !configChanged) {
            boolean isPlaying = syncer.isPlaying();
            removeListeners();
            onLeavingApp(isPlaying);
            if (!isPlaying) {
                stopSelf();
            }
        }
    }

    private void removeListeners() {
        audioCompletionHandler.removeListener();
        removeSyncListener();
    }

    private void onLeavingApp(boolean isPlaying) {
        broadcastStop(isPlaying);
        showNotification(isPlaying);
    }

    private void broadcastStop(boolean isPlaying) {
        if (!isPlaying) {
            new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().stop());
        }
    }

    private void showNotification(boolean isPlaying) {
        if (isPlaying) {
            NotificationService.start(this, syncer.getItemId(), isPlaying);
        }
    }

    private void removeSyncListener() {
        syncer.removeSyncListener();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        this.configChanged = true;
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
