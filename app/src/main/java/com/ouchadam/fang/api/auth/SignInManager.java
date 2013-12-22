package com.ouchadam.fang.api.auth;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

import com.ouchadam.fang.api.generic.KantoJsonClient;

public class SignInManager implements AuthValidator {

    private final AccountManager accountManager;
    private final AuthHandler authHandler;

    public SignInManager(AccountManager accountManager, AuthHandler authHandler) {
        this.accountManager = accountManager;
        this.authHandler = authHandler;
    }

    public static SignInManager from(Context context, AuthResult authResult) {
        try {
            AuthHandler authHandler = new AuthHandler(context, authResult);
            return new SignInManager(AccountManager.get(context), authHandler);
        } catch (MissingGooglePlayServicesException e) {
            throw new RuntimeException();
        }
    }

    public void onDestroy() {
        authHandler.onDestroy();
    }

    public KantoJsonClient getServiceHandle() {
        return authHandler.getServiceHandle();
    }

    @Override
    public void validateAuthOrThrow() throws AuthenticationException {
        authHandler.validateAuthOrThrow();
    }

    public interface SignInMethod {
        void noAccount();
        void singleAccount(Account account);
        void multiAccounts();
    }

    public void handleSignin(AccountSelector accountSelector) {
        SignInMethodFinder signInMethodFinder = new SignInMethodFinder(accountManager);
        FooSignin fooSignin = new FooSignin(authHandler, accountSelector);
        signInMethodFinder.invokeSignInMethod(fooSignin);
    }

    public void performAuth(String emailAccount) {
        authHandler.performAuthCheck(emailAccount);
    }

    private static class FooSignin implements SignInMethod {

        private final AuthHandler authHandler;
        private final AccountSelector accountSelector;

        private FooSignin(AuthHandler authHandler, AccountSelector accountSelector) {
            this.authHandler = authHandler;
            this.accountSelector = accountSelector;
        }

        @Override
        public void noAccount() {
//                Toast.makeText(activity, R.string.toast_no_google_accounts_registered, Toast.LENGTH_LONG).show();
        }

        @Override
        public void singleAccount(Account account) {
            authHandler.performAuthCheck(account.name);
        }

        @Override
        public void multiAccounts() {
            accountSelector.showAccounts();
        }
    }

}
