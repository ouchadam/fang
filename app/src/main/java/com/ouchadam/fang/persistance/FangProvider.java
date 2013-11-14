package com.ouchadam.fang.persistance;

import android.content.ContentValues;
import android.net.Uri;

import com.ouchadam.fang.persistance.database.Uris;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FangProvider extends SQLiteContentProviderImpl {

    public static final Uri AUTHORITY = Uri.parse("content://com.ouchadam.fang/");

    public static Uri getUri(Uris uri) {
        return AUTHORITY.buildUpon().appendPath(uri.name()).build();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = super.update(uri, values, selection, selectionArgs);
        if (uri.equals(getUri(Uris.PLAYLIST)) || uri.equals(getUri(Uris.CHANNEL))) {
            notifyUriChange(getUri(Uris.FULL_ITEM));
        }
        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = super.delete(uri, selection, selectionArgs);
        if (uri.equals(getUri(Uris.PLAYLIST))) {
            notifyUriChange(getUri(Uris.FULL_ITEM));
        }
        return result;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri result = super.insert(uri, values);
        if (uri.equals(getUri(Uris.ITEM))) {
            notifyUriChange(getUri(Uris.FULL_ITEM));
        }
        return result;
    }
}
