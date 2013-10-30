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
        if (isWithinApp()) {
            listener.onSync(playerHandler.asSyncEvent());
        }
    }

    public boolean isWithinApp() {
        return listener != null;
    }

    public void removeSyncListener() {
        listener = null;
    }

}
