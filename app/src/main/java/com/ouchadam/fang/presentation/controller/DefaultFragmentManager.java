package com.ouchadam.fang.presentation.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;

import com.ouchadam.fang.presentation.drawer.Fragments;

class DefaultFragmentManager {

    public static final String KEY_DEFAULT_FRAGMENT = "defaultFragment";
    private final SharedPreferences preferences;

    public static DefaultFragmentManager from(Context context) {
        return new DefaultFragmentManager(PreferenceManager.getDefaultSharedPreferences(context));
    }

    DefaultFragmentManager(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public Fragment getDefaultFragment() {
        String defaultFragment = preferences.getString(KEY_DEFAULT_FRAGMENT, Fragments.LATEST.name());
        return Fragments.valueOf(defaultFragment).get();
    }

    public void setDefaultFragment(Fragments fragment) {
        preferences.edit().putString(KEY_DEFAULT_FRAGMENT, fragment.name()).apply();
    }

}
