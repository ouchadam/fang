package com.ouchadam.fang.sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

public class FangAccountManager {

    private static final String AUTHORITY = "com.ouchadam.fang.feedsync";
    private static final String ACCOUNT_TYPE = "fang.com";
    private static final String ACCOUNT = "dummyaccount";

    public static Account CreateSyncAccount(Context context) {
        Account newAccount = new Account(ACCOUNT, ACCOUNT_TYPE);

        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */
            return newAccount;
        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
            return null;
        }
    }

}
