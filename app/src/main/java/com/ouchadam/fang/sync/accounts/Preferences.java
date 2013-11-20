package com.ouchadam.fang.sync.accounts;

import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.persistance.BasePreferenceActivity;

public class Preferences extends BasePreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromXml(R.xml.fang_settings);
    }
}
