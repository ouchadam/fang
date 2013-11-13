package com.ouchadam.fang.audio;

public class AudioCompletionHandler implements OnPlayCompletionListener {

    private final ServiceLocation serviceLocation;
    private CompletionListener onCompletionListener;

    AudioCompletionHandler(ServiceLocation serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    @Override
    public void onCompletion(PlayerHandler playerHandler) {
        if (playerHandler.lastInPlaylist()) {
            if (serviceLocation.isWithinApp()) {
                playerHandler.completeAudio();
                onCompletionListener.onComplete();
            } else {
                playerHandler.onStop();
            }
        } else {
            playerHandler.completeAndPlayNext();
        }
    }

    public void setActivityListener(CompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void removeListener() {
        this.onCompletionListener = null;
    }
}
