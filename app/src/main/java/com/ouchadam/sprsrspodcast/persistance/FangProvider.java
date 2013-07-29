package com.ouchadam.sprsrspodcast.persistance;

import android.net.Uri;

import com.ouchadam.sprsrspodcast.persistance.database.Uris;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FangProvider extends SQLiteContentProviderImpl {

    public static final Uri AUTHORITY = Uri.parse("content://com.ouchadam.fang/");

    public static Uri getUri(Uris uris) {
        return AUTHORITY.buildUpon().appendPath(uris.name()).build();
    }

}
