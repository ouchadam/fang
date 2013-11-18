package com.ouchadam.fang.sync;

import android.accounts.Account;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.SyncStatusObserver;

import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.presentation.controller.PullToRefreshExposer;

public class SyncAdapterStatusChangeHandler implements SyncStatusObserver {

    private final Activity activity;
    private final PullToRefreshExposer pullToRefreshExposer;

    public SyncAdapterStatusChangeHandler(Activity activity, PullToRefreshExposer pullToRefreshExposer) {
        this.activity = activity;
        this.pullToRefreshExposer = pullToRefreshExposer;
    }

    @Override
    public void onStatusChanged(int i) {
        activity.runOnUiThread(new Runnable() {
            /**
             * The SyncAdapter runs on a background thread. To update the UI, onStatusChanged()
             * runs on the UI thread.
             */
            @Override
            public void run() {
                // Create a handle to the account that was created by
                // SyncService.CreateSyncAccount(). This will be used to query the system to
                // see how the sync status has changed.
                Account account = GenericAccountService.GetAccount();
                if (noAccountFound(account)) {
                    // GetAccount() returned an invalid value. This shouldn't happen, but
                    // we'll set the status to "not refreshing".
                    pullToRefreshExposer.setRefreshing(false);
                    return;
                }

                // Test the ContentResolver to see if the sync adapter is active or pending.
                // Set the state of the refresh button accordingly.
                boolean syncActive = ContentResolver.isSyncActive(account, FangProvider.AUTHORITY.getAuthority());
                boolean syncPending = ContentResolver.isSyncPending(account, FangProvider.AUTHORITY.getAuthority());
                pullToRefreshExposer.setRefreshing(syncActive || syncPending);
            }
        });
    }

    private boolean noAccountFound(Account account) {
        return account == null;
    }
}
