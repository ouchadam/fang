package com.ouchadam.fang.sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class FangSyncService extends Service {

    private static final Object sSyncAdapterLock = new Object();
    private static FangFeedSyncAdapter sSyncAdapter = null;

    @Override
    public void onCreate() {
        synchronized (sSyncAdapterLock) {
            if (sSyncAdapter == null) {
                sSyncAdapter = new FangFeedSyncAdapter(getApplicationContext(), true);
            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return sSyncAdapter.getSyncAdapterBinder();
    }

}
