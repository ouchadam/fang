package com.ouchadam.fang.presentation.controller;

import android.app.Activity;
import android.media.AudioManager;

class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {

    private final AudioManager audioManager;

    AudioFocusManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public void requestFocus(Activity activity) {
        int result = audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
            activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
        }
        // TODO as soon as the app is in the foreground
    }

    public void abandonFocus() {
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int i) {
        // TODO
    }
}
