package com.ouchadam.fang.audio;

import android.os.Handler;
import android.os.Looper;

class Syncer {

    private static final Handler MAIN_THREAD = new Handler(Looper.getMainLooper());
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
            startSync();
        }
    }

    private void startSync() {
        MAIN_THREAD.post(new Runnable() {
            @Override
            public void run() {
                listener.onSync(playerHandler.asSyncEvent());
            }
        });
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
