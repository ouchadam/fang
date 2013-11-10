package com.ouchadam.fang.audio;

class Syncer {

    private final PlayerHandler playerHandler;

    private AudioServiceBinder.OnStateSync listener;

    Syncer(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    public void setSyncListener(AudioServiceBinder.OnStateSync listener) {
        this.listener = listener;
    }

    public void sync() {
        boolean canSync = playerHandler.asSyncEvent().isFresh();
        if (!canSync) {
            listener.onSync(playerHandler.asSyncEvent());
        }
    }

    public void removeSyncListener() {
        listener = null;
    }

    public boolean isPlaying() {
        return playerHandler.asSyncEvent().isPlaying;
    }

    public long getItemId() {
        return playerHandler.asSyncEvent().itemId;
    }
}
