/*
 * Copyright 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ouchadam.fang.sync;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

import com.ouchadam.fang.Log;
import com.ouchadam.fang.debug.SyncDataHandler;
import com.ouchadam.fang.debug.ThreadTracker;

public class FangSyncAdapter extends AbstractThreadedSyncAdapter {

    private final Context context;
    private boolean syncing;

    public FangSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
        this.syncing = false;
    }

    public FangSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.context = context;
        this.syncing = false;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        SyncDataHandler.from(context, threadsCompleteListener).handleSync(extras, syncResult);
        syncing = true;

        long syncStart = System.currentTimeMillis();
        Log.e("XXX : " + "syncing start : " + syncResult);
        while (syncing) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        Log.e("XXX : " + "syncing end : " + (System.currentTimeMillis() - syncStart));
    }

    private final ThreadTracker.OnAllThreadsComplete threadsCompleteListener = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
            syncing = false;
        }
    };

}
