package com.ouchadam.fang.audio;

public class AudioCompletionHandler implements OnPlayCompletionListener {

    private final ServiceLocation serviceLocation;
    private CompletionListener onCompletionListener;

    AudioCompletionHandler(ServiceLocation serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    @Override
    public void onCompletion(PlayerHandler playerHandler) {
        if (serviceLocation.isWithinApp()) {
            playerHandler.completeAudio();
            onCompletionListener.onComplete();
        } else {
            playerHandler.onStop();
        }
    }

    public void setActivityListener(CompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }
}
