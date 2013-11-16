package com.ouchadam.fang.setting;

import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

import com.ouchadam.fang.R;

public class SettingsFragment extends PreferenceFragment {

    private static final String ENABLE_PAUSE_REWIND = "enablePauseRewind";
    private static final String PAUSE_REWIND_AMOUNT = "pauseRewindAmount";

    private Preference rewindTimes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fang_settings);

        CheckBoxPreference enablePauseRewind = (CheckBoxPreference) getPreferenceManager().findPreference(ENABLE_PAUSE_REWIND);
        enablePauseRewind.setOnPreferenceChangeListener(onEnablePauseRewindListener);

        rewindTimes = getPreferenceManager().findPreference(PAUSE_REWIND_AMOUNT);
        rewindTimes.setEnabled(enablePauseRewind.isChecked());
    }

    private final Preference.OnPreferenceChangeListener onEnablePauseRewindListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object checked) {
            rewindTimes.setEnabled((Boolean) checked);
            return true;
        }
    };
}
