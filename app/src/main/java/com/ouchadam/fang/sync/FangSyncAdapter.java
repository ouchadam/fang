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
import android.content.Intent;
import android.content.SyncResult;
import android.os.Bundle;

import com.ouchadam.fang.Log;
import com.ouchadam.fang.debug.SyncDataHandler;
import com.ouchadam.fang.debug.ThreadTracker;

import java.io.IOException;

public class FangSyncAdapter extends AbstractThreadedSyncAdapter {

    private final Context context;

    public FangSyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        this.context = context;
    }

    public FangSyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        this.context = context;
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {
        SyncDataHandler syncDataHandler = SyncDataHandler.from(context, threadsCompleteListener, syncError);
        syncDataHandler.handleSync(extras);
    }

    private final ThreadTracker.OnAllThreadsComplete threadsCompleteListener = new ThreadTracker.OnAllThreadsComplete() {
        @Override
        public void onFinish() {
            endSync();
        }
    };

    private final SyncDataHandler.SyncError syncError = new SyncDataHandler.SyncError() {
        @Override
        public void onError(IOException e) {
            endSync();
        }
    };

    private synchronized void endSync() {
        context.sendBroadcast(new Intent("complete"));
    }

}
