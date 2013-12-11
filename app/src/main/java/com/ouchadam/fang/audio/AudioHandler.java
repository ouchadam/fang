package com.ouchadam.fang.audio;

import android.net.Uri;

import com.ouchadam.fang.Log;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.AudioFocusManager;

class AudioHandler {

    private static final int FORWARD_REWIND_AMOUNT = 15000;

    private final FangPlayer fangPlayer;
    private final AudioFocusManager audioFocusManager;
    private final AudioSync audioSync;
    private final Playlist playlist;
    private final AudioStateManager audioStateManager;
    private final RemoteHelper remoteHelper;
    private final PauseRewinder pauseRewinder;
    private final EventQueue eventQueue;

    private int playlistPosition;
    private MoveType lastMoveType;

    private enum MoveType {
        PREVIOUS,
        NEXT;
    }

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
        Log.d("onNewSource with position : " + playlistPosition);
        this.playlistPosition = playlistPosition;
        removeCurrentlyPlaying();
        playlist.load(onPlaylistPrepared);
    }

    private void removeCurrentlyPlaying() {
        if (fangPlayer.isPrepared()) {
            eventQueue.clear();
            if (audioStateManager.isPlaying()) {
                pauseAudio();
            }
            fangPlayer.release();
            Log.d("releasing player... " + "is prepared? : " + fangPlayer.isPrepared());
        }
    }

    private final Playlist.OnPlaylistPrepared onPlaylistPrepared = new Playlist.OnPlaylistPrepared() {
        @Override
        public void onPrepared() {
            playlist.moveTo(playlistPosition);
            if (playlist.isValid()) {
                if (playlist.currentItemIsValid()) {
                    setSource();
                    triggerQueue();
                } else {
                    Log.d("play list is valid but item isn't, trying next");
                    tryNext();
                }
            }
        }
    };

    private void tryNext() {
        if (getLastMoveType() == MoveType.NEXT) {
            onNext();
        } else {
            onPrevious();
        }
    }

    private MoveType getLastMoveType() {
        return this.lastMoveType;
    }

    private void triggerQueue() {
        Log.d("Triggering queue");
        eventQueue.dequeue(onEventHandler);
        Log.d("Queue finished");
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
        Log.d("onGoToPosition");
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
        Log.d("onPlay : isPrepared? " + fangPlayer.isPrepared());
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
        Log.d("onPlay with position" + "is prepared? " + isPrepared());
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
        Log.d("onPause");
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
        Log.d("onPlayPause");
        if (isPrepared()) {
            if (audioStateManager.isPlaying()) {
                onPause();
            } else {
                onPlay(fangPlayer.getPosition());
            }
        } else {

        }
    }

    public void onStop() {
        Log.d("onStop");
        stopAudio();
    }

    private void stopAudio() {
        audioStateManager.setIdle();
        audioFocusManager.abandonFocus();
        fangPlayer.release();
    }

    public void onReset() {
        Log.d("onReset");
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
        Log.d("onRewind");
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
        Log.d("onFastForward");
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
        return new SyncEvent(audioStateManager.isPlaying(), fangPlayer.getPosition(), playlist.getCurrentId());
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

    public void onNext() {
        this.lastMoveType = MoveType.NEXT;
        Log.d("onNext" + "current playlist position : " + playlistPosition);
        int nextPosition = playlistPosition + 1;
        movePosition(nextPosition);
    }

    public void onPrevious(PlayingItemStateManager playingItemStateManager) {
        Log.d("onPrevious : " + " current playlist position : " + playlistPosition);
        if (canMoveToPrevious()) {
            onPrevious();
        } else {
            goToPosition(PodcastPosition.idle());
            saveCurrentPlayState(playingItemStateManager);
        }
    }

    private void onPrevious() {
        this.lastMoveType = MoveType.PREVIOUS;
        int previousPosition = playlistPosition - 1;
        movePosition(previousPosition);
    }

    private boolean canMoveToPrevious() {
        return fangPlayer.getPosition().asPercentage() <= 1;
    }

    public void movePosition(int newPosition) {
        boolean isPlaying = audioStateManager.isPlaying();
        if (playlist.hasPosition(newPosition)) {
            setSource(newPosition);
            replay(isPlaying);
        }
    }

    private void replay(boolean isPlaying) {
        if (isPlaying) {
            onPlay();
        }
    }
}
