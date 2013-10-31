package com.ouchadam.fang.audio;

class Syncer {

    private final PlayerHandler playerHandler;
    private final ServiceLocation serviceLocation;

    private AudioServiceBinder.OnStateSync listener;

    Syncer(PlayerHandler playerHandler, ServiceLocation serviceLocation) {
        this.playerHandler = playerHandler;
        this.serviceLocation = serviceLocation;
    }

    public void setSyncListener(AudioServiceBinder.OnStateSync listener) {
        this.listener = listener;
    }

    public void sync() {
        if (serviceLocation.isWithinApp()) {
            listener.onSync(playerHandler.asSyncEvent());
        }
    }

    public void removeSyncListener() {
        listener = null;
    }

}
