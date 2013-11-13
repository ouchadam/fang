package com.ouchadam.fang.audio;

import android.util.Log;

class Syncer {

    private final PlayerHandler playerHandler;

    private OnStateSync listener;

    Syncer(PlayerHandler playerHandler) {
        this.playerHandler = playerHandler;
    }

    public void setSyncListener(OnStateSync listener) {
        this.listener = listener;
    }

    public void sync() {
        boolean canSync = !playerHandler.asSyncEvent().isFresh();
        if (canSync) {
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
