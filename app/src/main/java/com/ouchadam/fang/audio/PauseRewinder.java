package com.ouchadam.fang.audio;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

class PauseRewinder {

    private static final String KEY_ENABLE_PAUSE_REWIND = "enablePauseRewind";
    private static final String KEY_PAUSE_REWIND_AMOUNT = "pauseRewindAmount";

    private final SharedPreferences preferences;

    public static PauseRewinder from(Context context) {
        return new PauseRewinder(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public PauseRewinder(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public void handle(AudioHandler audioHandler) {
        if (shouldRewind()) {
            audioHandler.onRewind(getRewindAmount());
        }
    }

    private int getRewindAmount() {
        return convertSecondsToMs(getRewindAmountSeconds());
    }

    private boolean shouldRewind() {
        return preferences.getBoolean(KEY_ENABLE_PAUSE_REWIND, false);
    }

    private int convertSecondsToMs(int seconds) {
        return seconds * 1000;
    }

    private int getRewindAmountSeconds() {
        return Integer.valueOf(preferences.getString(KEY_PAUSE_REWIND_AMOUNT, "5"));
    }

}
