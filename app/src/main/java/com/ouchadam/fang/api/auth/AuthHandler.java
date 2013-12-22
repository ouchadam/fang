package com.ouchadam.fang.api.auth;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.common.base.Strings;
import com.ouchadam.kanto.api.Constants;
import com.ouchadam.kanto.api.generic.KantoJsonClient;
import com.ouchadam.kanto.presentation.MainActivity;

public class AuthHandler implements AuthValidator {

    private final AuthorizationCheckTask authTask;
    private final AuthResult externalAuthResult;
    private final GoogleAccountCredential credential;

    private String emailAccount = "";

    public AuthHandler(Context context, AuthResult externalAuthResult) throws MissingGooglePlayServicesException {
        this.externalAuthResult = externalAuthResult;
        validateGooglePlayServicesAvailability(context);
        credential = GoogleAccountCredential.usingAudience(context, Constants.AUDIENCE);
        authTask = new AuthorizationCheckTask(credential, innerAuthResult);
    }

    private void validateGooglePlayServicesAvailability(Context context) throws MissingGooglePlayServicesException {
        final int connectionStatusCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (GooglePlayServicesUtil.isUserRecoverableError(connectionStatusCode)) {
            throw new MissingGooglePlayServicesException(connectionStatusCode);
        }
    }

    private static void showGooglePlayServicesAvailabilityErrorDialog(final Activity activity, final int connectionStatusCode) {
        final int REQUEST_GOOGLE_PLAY_SERVICES = 0;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(connectionStatusCode, activity, REQUEST_GOOGLE_PLAY_SERVICES);
                dialog.show();
            }
        });
    }

    private final AuthResult innerAuthResult = new AuthResult() {
        @Override
        public void onSuccess(String emailAccount) {
            AuthHandler.this.emailAccount = emailAccount;
            externalAuthResult.onSuccess(emailAccount);
        }

        @Override
        public void onError() {
            AuthHandler.this.emailAccount = "";
            externalAuthResult.onError();
        }
    };

    @Override
    public void validateAuthOrThrow() throws AuthenticationException {
        if (!isSignedIn()) {
            throw new AuthenticationException();
        }
    }

    private boolean isSignedIn() {
        return !Strings.isNullOrEmpty(emailAccount);
    }

    public void performAuthCheck(String emailAccount) {
        authTask.cancelCurrent();
        authTask.execute(emailAccount);
    }

    public void onDestroy() {
        authTask.cancelCurrent();
    }

    public KantoJsonClient getServiceHandle() {
        Log.e(MainActivity.LOG_TAG, "Getting service handle");
        Log.i(MainActivity.LOG_TAG, "email = : " + emailAccount);
        credential.setSelectedAccountName(emailAccount);
        return Constants.getApiServiceHandle(credential);
    }

}
