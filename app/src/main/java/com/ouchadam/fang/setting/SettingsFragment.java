package com.ouchadam.fang.setting;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.ouchadam.fang.R;

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.fang_settings);
    }
}
