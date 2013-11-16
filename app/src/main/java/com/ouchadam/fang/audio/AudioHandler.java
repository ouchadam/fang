package com.ouchadam.fang.audio;

import android.net.Uri;
import android.util.Log;

import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.AudioFocusManager;

class AudioHandler {

    private static final int FORWARD_REWIND_AMOUNT = 10000;

    private final FangPlayer fangPlayer;
    private final AudioFocusManager audioFocusManager;
    private final AudioSync audioSync;
    private final Playlist playlist;
    private final AudioStateManager audioStateManager;
    private final RemoteHelper remoteHelper;
    private final PauseRewinder pauseRewinder;
    private final EventQueue eventQueue;

    private int playlistPosition;

    AudioHandler(FangPlayer fangPlayer, AudioFocusManager audioFocusManager, AudioSync audioSync, Playlist playlist, AudioStateManager audioStateManager, RemoteHelper remoteHelper, PauseRewinder pauseRewinder) {
        this.fangPlayer = fangPlayer;
        this.audioFocusManager = audioFocusManager;
        this.audioSync = audioSync;
        this.playlist = playlist;
        this.audioStateManager = audioStateManager;
        this.remoteHelper = remoteHelper;
        this.pauseRewinder = pauseRewinder;
        this.eventQueue = new EventQueue();
    }

    public void setSource(int playlistPosition) {
        Log.e("XXX", "onNewSource with position : " + playlistPosition);
        this.playlistPosition = playlistPosition;
        removeCurrentlyPlaying();
        playlist.load(onPlaylistPrepared);
    }

    private void removeCurrentlyPlaying() {
        if (fangPlayer.isPrepared()) {
            eventQueue.clear();
            if (audioStateManager.isPlayling()) {
                pauseAudio();
            }
            fangPlayer.release();
            Log.e("XXX", "releasing player... " + "is prepared? : " + fangPlayer.isPrepared());
        }
    }

    private final Playlist.OnPlaylistPrepared onPlaylistPrepared = new Playlist.OnPlaylistPrepared() {
        @Override
        public void onPrepared() {
            playlist.moveTo(playlistPosition);
            if (playlist.isValid() && playlist.currentItemIsValid()) {
                setSource();
                triggerQueue();
            }
        }
    };

    private void triggerQueue() {
        Log.e("XXX", "Triggering queue");
        eventQueue.dequeue(onEventHandler);
        Log.e("XXX", "Queue finished");
    }

    private final EventQueue.OnEvent onEventHandler = new EventQueue.OnEvent() {
        @Override
        public void onEvent(PlayerEvent playerEvent) {
            switch (playerEvent.getEvent()) {
                case PLAY:
                    if (playerEvent.getPosition() == null) {
                        onPlay();
                    } else {
                        onPlay(playerEvent.getPosition());
                    }
                    break;

                case GOTO:
                    goToPosition(playerEvent.getPosition());
                    break;
                default:
                    throw new IllegalAccessError("Event queue handler Not yet implemented for : " + playerEvent.getEvent().name());
            }
        }
    };

    private void setSource() {
        Playlist.PlaylistItem playItem = playlist.get();
        playlist.setCurrent(playItem.id);
        remoteHelper.setData(playItem);
        setAudioSource(playItem.source);
        goToPosition(getInitialPosition(playItem));
        audioStateManager.setPositionVanilla();
        sync(new PlayerEvent.Factory().newSource(playlist.getCurrentPosition(), "PLAYLIST"));
    }

    private PodcastPosition getInitialPosition(Playlist.PlaylistItem playItem) {
        return playItem.podcastPosition.isCompleted() ? PodcastPosition.idle() : playItem.podcastPosition;
    }

    private void setAudioSource(Uri uri) {
        fangPlayer.setSource(uri);
    }

    private void sync(PlayerEvent playerEvent) {
        audioSync.onSync(playlist.getCurrentId(), playerEvent);
    }

    public void goToPosition(PodcastPosition position) {
        Log.e("XXX", "onGoToPosition");
        PlayerEvent playerEvent = new PlayerEvent.Factory().goTo(position);
        if (fangPlayer.isPrepared()) {
            audioStateManager.setPositionShifted();
            fangPlayer.goTo(position);
            sync(playerEvent);
        } else {
            eventQueue.add(playerEvent);
        }
    }

    public void onPlay() {
        Log.e("XXX", "onPlay : isPrepared? " + fangPlayer.isPrepared());
        if (fangPlayer.isPrepared()) {
            onPlay(getPosition());
        } else {
            eventQueue.add(new PlayerEvent.Factory().play());
        }
    }

    private PodcastPosition getPosition() {
        return audioStateManager.hasVanillaPosition() ? playlist.get().podcastPosition : fangPlayer.getPosition();
    }

    public void onPlay(PodcastPosition position) {
        Log.e("XXX", "onPlay with position" + "is prepared? " + isPrepared());
        PlayerEvent playerEvent = new PlayerEvent.Factory().play(position);
        if (fangPlayer.isPrepared()) {
            play(position);
            sync(playerEvent);
        } else {
            eventQueue.add(playerEvent);
        }
    }

    private void play(PodcastPosition position) {
        audioStateManager.setPlaying();
        remoteHelper.setPlaying();
        audioFocusManager.requestFocus();
        fangPlayer.play(position);
        audioStateManager.setPositionShifted();
    }

    public void onPause() {
        Log.e("XXX", "onPause");
        pauseAudio();
        sync(new PlayerEvent.Factory().pause());
        pauseRewinder.handle(this);
    }

    private void pauseAudio() {
        audioStateManager.setIdle();
        remoteHelper.setPaused();
        fangPlayer.pause();
    }

    public void onPlayPause() {
        Log.e("XXX", "onPlayPause");
        if (audioStateManager.isPlayling()) {
            onPause();
        } else {
            onPlay(fangPlayer.getPosition());
        }
    }

    public void onStop() {
        Log.e("XXX", "onStop");
        stopAudio();
    }

    private void stopAudio() {
        audioStateManager.setIdle();
        audioFocusManager.abandonFocus();
        fangPlayer.release();
    }

    public void onReset() {
        Log.e("XXX", "onReset");
        stopAudio();
        playlist.resetCurrent();
    }

    public void playNext() {
        playlist.moveToNext();
        setSource();
        onPlay();
    }

    public void completeCurrent() {
        pauseAudio();
        sync(new PlayerEvent.Factory().pause());
    }

    public void onRewind() {
        Log.e("XXX", "onRewind");
        onRewind(FORWARD_REWIND_AMOUNT);
    }

    public void onRewind(int amountMs) {
        PodcastPosition currentPosition = fangPlayer.getPosition();
        PodcastPosition rewindPosition = new PodcastPosition(moderateRewind(amountMs, currentPosition), currentPosition.getDuration());
        goToPosition(rewindPosition);
    }

    private int moderateRewind(int amountMs, PodcastPosition currentPosition) {
        int rewindPosition = currentPosition.value() - amountMs;
        return rewindPosition > 0 ? rewindPosition : 0;
    }

    public void onFastForward() {
        Log.e("XXX", "onFastForward");
        PodcastPosition currentPosition = fangPlayer.getPosition();
        if (canFastForward(currentPosition)) {
            PodcastPosition forwardPosition = new PodcastPosition(currentPosition.value() + FORWARD_REWIND_AMOUNT, currentPosition.getDuration());
            goToPosition(forwardPosition);
        }
    }

    private boolean canFastForward(PodcastPosition currentPosition) {
        return currentPosition.value() + FORWARD_REWIND_AMOUNT < currentPosition.getDuration();
    }

    public void restore(PlayItem playItem) {
        if (playItem.isValid()) {
            playlist.setCurrent(playItem.getId());
            setSource(playItem.getPlaylistPosition());
        }
    }

    public boolean hasNext() {
        return playlist.hasNext();
    }

    public SyncEvent asSyncEvent() {
        return playlist.currentItemIdIsValid() ? createValidSyncEvent() : SyncEvent.fresh();
    }

    private SyncEvent createValidSyncEvent() {
        return !fangPlayer.isPrepared() ? SyncEvent.idle(playlist.getCurrentId()) : createCurrentSyncEvent();
    }

    private SyncEvent createCurrentSyncEvent() {
        return new SyncEvent(audioStateManager.isPlayling(), fangPlayer.getPosition(), playlist.getCurrentId());
    }

    public void saveCurrentPlayState(PlayingItemStateManager itemStateManager) {
        if (!audioStateManager.hasVanillaPosition()) {
            itemStateManager.persist(playlist.getCurrentId(), fangPlayer.getPosition(), fangPlayer.getSource(), playlist.getCurrentPosition());
        }
    }

    public void saveCompletedState(PlayingItemStateManager itemStateManager) {
        itemStateManager.persist(playlist.getCurrentId(), getCompletedPosition(), fangPlayer.getSource(), playlist.getCurrentPosition());
    }

    private PodcastPosition getCompletedPosition() {
        return fangPlayer.getPosition().asCompleted();
    }

    public boolean isPrepared() {
        return fangPlayer.isPrepared();
    }
}
