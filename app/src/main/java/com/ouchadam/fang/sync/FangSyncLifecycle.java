package com.ouchadam.fang.sync;

import android.app.Activity;
import android.content.ContentResolver;

import com.ouchadam.fang.presentation.controller.PullToRefreshExposer;

public class FangSyncLifecycle {

    private FangSyncStatusObserver mSyncStatusObserver;
    private Object mSyncObserverHandle;

    public void init(Activity activity, PullToRefreshExposer pullToRefreshExposer) {
        FangSyncHelper.createSyncAccount(activity);
        mSyncStatusObserver = new FangSyncStatusObserver(activity, pullToRefreshExposer);
    }

    public void onResume() {
        mSyncStatusObserver.onStatusChanged(0);
        final int mask = ContentResolver.SYNC_OBSERVER_TYPE_PENDING | ContentResolver.SYNC_OBSERVER_TYPE_ACTIVE;
        mSyncObserverHandle = ContentResolver.addStatusChangeListener(mask, mSyncStatusObserver);
    }

    public void onPause() {
        if (mSyncObserverHandle != null) {
            ContentResolver.removeStatusChangeListener(mSyncObserverHandle);
            mSyncObserverHandle = null;
        }
    }
}
