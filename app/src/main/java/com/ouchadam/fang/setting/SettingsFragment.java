package com.ouchadam.fang.setting;

import android.accounts.Account;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.text.TextUtils;
import android.widget.Toast;

import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.api.auth.AccountSelector;

public class SettingsFragment extends PreferenceFragment {

    private static final String ENABLE_PAUSE_REWIND = "enablePauseRewind";
    private static final String PAUSE_REWIND_AMOUNT = "pauseRewindAmount";
    private static final String ACCOUNT_SELECTOR = "accountSelector";

    private Preference rewindTimes;
    private AccountSelector accountSelector;
    private Preference accountSelectorPref;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.fang_settings);

        accountSelectorPref = findPref(ACCOUNT_SELECTOR);
        String currentAccountName = getCurrentAccountName();
        if (currentAccountName != null && !TextUtils.isEmpty(currentAccountName)) {
            accountSelectorPref.setTitle(currentAccountName);
        }

        accountSelector = new AccountSelector(this);

        accountSelectorPref.setOnPreferenceClickListener(onAccountSelectorClicked);

        CheckBoxPreference enablePauseRewind = (CheckBoxPreference) findPref(ENABLE_PAUSE_REWIND);
        enablePauseRewind.setOnPreferenceChangeListener(onEnablePauseRewindListener);

        rewindTimes = findPref(PAUSE_REWIND_AMOUNT);
        rewindTimes.setEnabled(enablePauseRewind.isChecked());
    }

    private Preference findPref(String key) {
        return findPreference(key);
    }

    private final Preference.OnPreferenceChangeListener onEnablePauseRewindListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object checked) {
            rewindTimes.setEnabled((Boolean) checked);
            return true;
        }
    };

    private final Preference.OnPreferenceClickListener onAccountSelectorClicked = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            accountSelector.showAccounts();
            return true;
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult fragment");
        accountSelector.onActivityResult(requestCode, resultCode, data, accountSelected);
    }

    private final AccountSelector.AccountSelected accountSelected = new AccountSelector.AccountSelected() {
        @Override
        public void onAccount(Account account) {
            accountSelectorPref.setTitle(account.name);
            Toast.makeText(getActivity(), "Selected : " + account.name, Toast.LENGTH_SHORT).show();
            storeAccount(account);
        }
    };

    private void storeAccount(Account account) {
        SharedPreferences sharedPreferences = getPreferences();
        sharedPreferences.edit().putString(ACCOUNT_SELECTOR, account.name).apply();
    }

    private String getCurrentAccountName() {
        SharedPreferences sharedPreferences = getPreferences();
        return sharedPreferences.getString(ACCOUNT_SELECTOR, "");
    }

    private SharedPreferences getPreferences() {
        return getActivity().getPreferences(Context.MODE_PRIVATE);
    }

}
