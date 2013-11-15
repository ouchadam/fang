package com.ouchadam.fang.audio;

import android.net.Uri;

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

    AudioHandler(FangPlayer fangPlayer, AudioFocusManager audioFocusManager, AudioSync audioSync, Playlist playlist, AudioStateManager audioStateManager, RemoteHelper remoteHelper) {
        this.fangPlayer = fangPlayer;
        this.audioFocusManager = audioFocusManager;
        this.audioSync = audioSync;
        this.playlist = playlist;
        this.audioStateManager = audioStateManager;
        this.remoteHelper = remoteHelper;
    }

    public void setSource(int playlistPosition) {
        playlist.load();
        playlist.moveTo(playlistPosition);
        if (playlist.isValid() && playlist.currentItemIsValid()) {
            setSource();
        }
    }

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
        audioStateManager.setPositionShifted();
        fangPlayer.goTo(position);
        sync(new PlayerEvent.Factory().goTo(position));
    }

    public void onPlay() {
        onPlay(getPosition());
    }

    private PodcastPosition getPosition() {
        return audioStateManager.hasVanillaPosition() ? playlist.get().podcastPosition : fangPlayer.getPosition();
    }

    public void onPlay(PodcastPosition position) {
        play(position);
        sync(new PlayerEvent.Factory().play(position));
    }

    private void play(PodcastPosition position) {
        audioStateManager.setPlaying();
        remoteHelper.setPlaying();
        audioFocusManager.requestFocus();
        fangPlayer.play(position);
        audioStateManager.setPositionShifted();
    }

    public void onPause() {
        pauseAudio();
        sync(new PlayerEvent.Factory().pause());
    }

    private void pauseAudio() {
        audioStateManager.setIdle();
        remoteHelper.setPaused();
        fangPlayer.pause();
    }

    public void onPlayPause() {
        if (audioStateManager.isPlayling()) {
            onPause();
        } else {
            onPlay(fangPlayer.getPosition());
        }
    }

    public void onStop() {
        stopAudio();
    }

    private void stopAudio() {
        audioStateManager.setIdle();
        audioFocusManager.abandonFocus();
        fangPlayer.release();
    }

    public void onReset() {
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
        PodcastPosition currentPosition = fangPlayer.getPosition();
        PodcastPosition rewindPosition = new PodcastPosition(moderateRewind(currentPosition), currentPosition.getDuration());
        goToPosition(rewindPosition);
    }

    private int moderateRewind(PodcastPosition currentPosition) {
        int rewindPosition = currentPosition.value() - FORWARD_REWIND_AMOUNT;
        return rewindPosition > 0 ? rewindPosition : 0;
    }

    public void onFastForward() {
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
