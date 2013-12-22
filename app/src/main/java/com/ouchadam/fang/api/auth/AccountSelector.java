package com.ouchadam.fang.api.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.common.AccountPicker;

public class AccountSelector {

    private final static int REQUEST_CODE = 2222;
    private final Fragment activity;

    private Account currentAccount;

    public interface AccountSelected {
        void onAccount(Account accountName);
    }

    public AccountSelector(Fragment activity) {
        this.activity = activity;
        this.currentAccount = null;
    }

    public void showAccounts() {
        Intent accountSelector = AccountPicker.newChooseAccountIntent(
                getCurrentAccount(),
                null,
                new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE},
                false,
                "Select the account to access Google Compute Engine API.",
                null,
                null,
                null);
        activity.startActivityForResult(accountSelector, REQUEST_CODE);
    }

    private Account getCurrentAccount() {
        return currentAccount;
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data, AccountSelected onAccount) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            String accountName = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
            currentAccount = new Account(accountName, GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
            onAccount.onAccount(currentAccount);
        }
    }

}
