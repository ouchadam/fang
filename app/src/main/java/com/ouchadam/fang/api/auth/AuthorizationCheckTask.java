package com.ouchadam.fang.api.auth;

import android.os.AsyncTask;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;

import java.io.IOException;

class AuthorizationCheckTask implements AuthResult {

    private final GoogleAccountCredential credential;
    private final AuthResult authResult;

    private InnerTask currentTask;

    @Override
    public void onSuccess(String emailAccount) {
        cancelCurrent();
        this.authResult.onSuccess(emailAccount);
    }

    @Override
    public void onError() {
        cancelCurrent();
        this.authResult.onError();
    }

    AuthorizationCheckTask(GoogleAccountCredential credential, AuthResult authResult) {
        this.credential = credential;
        this.authResult = authResult;
    }

    public void execute(String emailAccount) {
        cancelCurrent();
        currentTask = new InnerTask(credential, authResult);
        currentTask.execute(emailAccount);
    }

    public void cancelCurrent() {
        if (currentTask != null) {
            currentTask.cancelCurrent();
            currentTask = null;
        }
    }

    private static class InnerTask extends AsyncTask<String, Void, Boolean> {

        private final GoogleAccountCredential credential;
        private final AuthResult authResult;

        private String emailAccount;

        InnerTask(GoogleAccountCredential credential, AuthResult authResult) {
            this.credential = credential;
            this.authResult = authResult;
        }

        protected void onPreExecute() {
            super.onPreExecute();
            emailAccount = null;
        }

        @Override
        protected Boolean doInBackground(String... emailAccounts) {
            emailAccount = validateEmail(emailAccounts[0]);
            try {
                credential.setSelectedAccountName(emailAccount);
                String accessToken = credential.getToken();
                return true;
            } catch (GoogleAuthException unrecoverableException) {
                return false;
            } catch (IOException ioException) {
                return false;
            }
        }

        private String validateEmail(String emailAccount) {
            if (Strings.isNullOrEmpty(emailAccount)) {
                throw new IllegalArgumentException("Must supply a valid email");
            }
            return emailAccount;
        }

        @Override
        protected void onPostExecute(Boolean success) {
            if (success) {
                authResult.onSuccess(emailAccount);
            } else {
                authResult.onError();
            }
        }

        public void cancelCurrent() {
            cancel(true);
        }

    }

}
