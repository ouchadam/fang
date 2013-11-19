/*
 * Copyright 2013 Google Inc.
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
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;
import android.preference.PreferenceManager;

import com.ouchadam.fang.debug.FeedServiceInfo;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.sync.accounts.GenericAccountService;

public class FangSyncHelper {

    private static final long HOUR_IN_SECONDS = 60 * 60;
    private static final long DAY_IN_SECONDS = HOUR_IN_SECONDS * 24;

    private static final String CONTENT_AUTHORITY = FangProvider.AUTHORITY;
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    private static final int SYNCABLE = 1;

    public static void createSyncAccount(Context context) {
        boolean newAccount = false;
        boolean setupComplete = PreferenceManager.getDefaultSharedPreferences(context).getBoolean(PREF_SETUP_COMPLETE, false);

        Account account = GenericAccountService.GetAccount();
        AccountManager accountManager = (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, SYNCABLE);
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            ContentResolver.addPeriodicSync(account, CONTENT_AUTHORITY, new Bundle(), DAY_IN_SECONDS);
            newAccount = true;
        }

        if (newAccount || !setupComplete) {
            PreferenceManager.getDefaultSharedPreferences(context).edit().putBoolean(PREF_SETUP_COMPLETE, true).commit();
        }
    }

    public static void forceRefresh(FeedServiceInfo.Type type) {
        Bundle bundle = new Bundle();
        bundle.putString("type", type.name());
        forceRefresh(bundle);
    }

    public static void forceRefresh(Bundle extras) {
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        extras.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(GenericAccountService.GetAccount(), CONTENT_AUTHORITY, extras);                                      // Extras
    }
}
