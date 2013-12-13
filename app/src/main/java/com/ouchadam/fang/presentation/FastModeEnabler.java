package com.ouchadam.fang.presentation;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

class FastModeEnabler {

    private static final String KEY_FAST_MODE_ENABLED = "fastModeEnabled";
    private static final boolean DEFAULT_DISABLED = false;

    private final SharedPreferences preferences;

    public static FastModeEnabler from(Context context) {
        return new FastModeEnabler(PreferenceManager.getDefaultSharedPreferences(context));
    }

    public FastModeEnabler(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public boolean isEnabled() {
        return preferences.getBoolean(KEY_FAST_MODE_ENABLED, DEFAULT_DISABLED);
    }

}
