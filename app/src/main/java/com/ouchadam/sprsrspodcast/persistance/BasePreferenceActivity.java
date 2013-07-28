package com.ouchadam.sprsrspodcast.persistance;

import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.view.MenuItem;

public class BasePreferenceActivity extends PreferenceActivity {

    @SuppressWarnings("deprecation")
    protected void addPreferencesFromXml(int id) {
        addPreferencesFromResource(id);
    }

    @SuppressWarnings("deprecation")
    protected void setPreferenceListener(String key, Preference.OnPreferenceClickListener listener) {
        findPreference(key).setOnPreferenceClickListener(listener);
    }

    @SuppressWarnings("deprecation")
    protected Preference findPreferenceBy(String key) {
        return findPreference(key);
    }

    @SuppressWarnings("deprecation")
    protected void setPreferenceChangeListener(String key, Preference.OnPreferenceChangeListener listener) {
        findPreference(key).setOnPreferenceChangeListener(listener);
    }

    @SuppressWarnings("deprecation")
    protected void setPreferenceTitle(String key, String title) {
        findPreference(key).setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}