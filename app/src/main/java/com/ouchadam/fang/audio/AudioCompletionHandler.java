package com.ouchadam.fang.audio;

public class AudioCompletionHandler implements OnPlayCompletionListener {

    private final ServiceLocation serviceLocation;

    AudioCompletionHandler(ServiceLocation serviceLocation) {
        this.serviceLocation = serviceLocation;
    }

    @Override
    public void onCompletion(PlayerHandler playerHandler) {
        if (serviceLocation.isWithinApp()) {
            playerHandler.completeAudio();
        } else {
            playerHandler.onStop();
        }
    }
}
