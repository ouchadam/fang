package com.ouchadam.fang.api.auth;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.android.gms.auth.GoogleAuthUtil;

public class SignInMethodFinder {

    private final AccountManager accountManager;

    public SignInMethodFinder(AccountManager accountManager) {
        this.accountManager = accountManager;
    }

    void invokeSignInMethod(SignInManager.SignInMethod callbacks) {
        AccountsWrapper googleAccounts = getGoogleAccounts();
        switch (googleAccounts.getType()) {
            case SINGLE:
                callbacks.singleAccount(googleAccounts.getSingle());
                break;

            case MULTI:
                callbacks.multiAccounts();
                break;

            case NONE:
                callbacks.noAccount();
                break;

            default:
                throw new MissingAccountException();
        }
    }

    private AccountsWrapper getGoogleAccounts() {
        Account[] accounts = accountManager.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
        return new AccountsWrapper(accounts);
    }

    private static class AccountsWrapper {

        private static final int NO_ACCOUNTS = 0;
        private static final int SINGLE_ACCOUNT = 1;
        private static final int SINGLE_ACCOUNT_INDEX = 0;

        private final Account[] accounts;

        enum CountType {
            SINGLE,
            MULTI,
            NONE;

            static CountType from(int value) {
                switch (value) {
                    case NO_ACCOUNTS:
                        return NONE;

                    case SINGLE_ACCOUNT:
                        return SINGLE;

                    default:
                        return MULTI;
                }
            }

        }
        AccountsWrapper(Account[] accounts) {
            this.accounts = accounts;
        }

        public int size() {
            return accounts == null || accounts.length < 1 ? NO_ACCOUNTS : accounts.length;
        }

        public CountType getType() {
            return CountType.from(size());
        }

        public Account getSingle() {
            return accounts[SINGLE_ACCOUNT_INDEX];
        }

    }

}
