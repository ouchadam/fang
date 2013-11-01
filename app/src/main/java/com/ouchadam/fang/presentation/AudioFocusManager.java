package com.ouchadam.fang.presentation;

import android.app.Activity;
import android.media.AudioManager;

public class AudioFocusManager implements AudioManager.OnAudioFocusChangeListener {

    private final AudioManager audioManager;


    public enum FocusResult {
        DENIED,
        GRANTED;

        public static FocusResult from(int requestFocusResult) {
            return requestFocusResult == AudioManager.AUDIOFOCUS_REQUEST_GRANTED ? GRANTED : DENIED;
        }
    }

    public AudioFocusManager(AudioManager audioManager) {
        this.audioManager = audioManager;
    }

    public FocusResult requestFocus() {
        return FocusResult.from(audioManager.requestAudioFocus(this, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN));
    }

    public void abandonFocus() {
        audioManager.abandonAudioFocus(this);
    }

    @Override
    public void onAudioFocusChange(int i) {
        // TODO
    }
}
