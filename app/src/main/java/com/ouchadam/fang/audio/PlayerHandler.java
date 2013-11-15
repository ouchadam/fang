package com.ouchadam.fang.audio;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.notification.FangNotification;
import com.ouchadam.fang.presentation.AudioFocusManager;

class PlayerHandler implements PlayerEventReceiver.PlayerEventCallbacks {

    private final PlayingItemStateManager itemStateManager;
    private final FangNotification notification;
    private final ServiceManipulator serviceManipulator;
    private final ActivityCompletionCallback completionCallback;
    private final ServiceLocation serviceLocation;

    private final AudioHandler audioHandler;

    interface AudioSync {
        void onSync(long itemId, PlayerEvent playerEvent);
    }

    static PlayerHandler from(Context context, AudioSync audioSync, ServiceManipulator serviceManipulator, RemoteHelper remoteHelper, ActivityCompletionCallback completionCallback, ServiceLocation serviceLocation) {
        FangPlayer fangPlayer = new FangMediaPlayer(context, new PodcastPositionBroadcaster(context), null, new PodcastPlayerEventBroadcaster(context));
        AudioFocusManager focusManager = new AudioFocusManager((AudioManager) context.getSystemService(Context.AUDIO_SERVICE));
        PlayingItemStateManager itemStateManager = PlayingItemStateManager.from(context);
        FangNotification notification = FangNotification.from(context);
        Playlist playlist = Playlist.from(context);

        AudioHandler audioHandler = new AudioHandler(fangPlayer, focusManager, audioSync, playlist, new AudioStateManager(), remoteHelper);

        return new PlayerHandler(audioHandler, itemStateManager, notification, serviceManipulator, completionCallback, serviceLocation);
    }

    PlayerHandler(AudioHandler audioHandler, PlayingItemStateManager itemStateManager, FangNotification notification, ServiceManipulator serviceManipulator, ActivityCompletionCallback completionCallback, ServiceLocation serviceLocation) {
        this.audioHandler = audioHandler;
        this.itemStateManager = itemStateManager;
        this.notification = notification;
        this.serviceManipulator = serviceManipulator;
        this.completionCallback = completionCallback;
        this.serviceLocation = serviceLocation;
    }

    @Override
    public void onNewSource(int playlistPosition, String playlistName) {
        Log.e("!!!", "wants to play position : " + playlistPosition);
        audioHandler.setSource(playlistPosition);
    }


    @Override
    public void onPlay() {
        audioHandler.onPlay();
    }

    @Override
    public void onPlay(PodcastPosition position) {
        audioHandler.onPlay(position);
    }

    @Override
    public void onPause() {
        audioHandler.onPause();
        saveCurrentPlayState();
    }

    @Override
    public void onPlayPause() {
        audioHandler.onPlayPause();
    }

    @Override
    public void onStop() {
        saveCurrentPlayState();
        audioHandler.onStop();
        notification.dismiss();
        serviceManipulator.stop();
    }

    @Override
    public void onFastForward() {
        audioHandler.onFastForward();
    }

    @Override
    public void onRewind() {
        audioHandler.onRewind();
    }

    private void saveCurrentPlayState() {
        audioHandler.saveCurrentPlayState(itemStateManager);
    }

    @Override
    public void gotoPosition(PodcastPosition position) {
        audioHandler.goToPosition(position);
        saveCurrentPlayState();
    }

    @Override
    public void onComplete() {
        if (audioHandler.lastInPlaylist()) {
            if (serviceLocation.isWithinApp()) {
                audioHandler.completeAudio();
                saveCompletedState();
                completionCallback.onComplete();
            } else {
                audioHandler.onStop();
            }
        } else {
            completeAndPlayNext();
        }
    }

    private void completeAndPlayNext() {
        if (audioHandler.hasNext()) {
            saveCompletedState();
            audioHandler.completeAudio();
            audioHandler.completeAndPlayNext();
        }
    }

    private void saveCompletedState() {
        audioHandler.saveCompletedState(itemStateManager);
    }

    @Override
    public void onReset() {
        audioHandler.onReset();
        itemStateManager.resetCurrentItem();
    }

    public SyncEvent asSyncEvent() {
        return audioHandler.asSyncEvent();
    }

    public void restoreItem() {
        PlayItem playItem = itemStateManager.getStoredItem();
        audioHandler.restore(playItem);
    }

}
