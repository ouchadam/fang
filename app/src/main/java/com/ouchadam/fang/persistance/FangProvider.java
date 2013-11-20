package com.ouchadam.fang.persistance;

import android.content.ContentValues;
import android.net.Uri;
import android.widget.AbsListView;

import com.ouchadam.fang.persistance.database.Uris;

import novoda.lib.sqliteprovider.provider.SQLiteContentProviderImpl;

public class FangProvider extends SQLiteContentProviderImpl {

    public static final String AUTHORITY = "com.ouchadam.fang";
    public static final Uri BASE_URI = Uri.parse("content://" + AUTHORITY);

    public static Uri getUri(Uris uri) {
        return BASE_URI.buildUpon().appendPath(uri.name()).build();
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int result = super.update(uri, values, selection, selectionArgs);
        if (uri.equals(getUri(Uris.PLAYLIST))) {
            notifyUriChange(getUri(Uris.FULL_ITEM));
        }

        if (uri.equals(getUri(Uris.CHANNEL))) {
            notifyUriChange(getUri(Uris.FULL_CHANNEL));
        }

        return result;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int result = super.delete(uri, selection, selectionArgs);
        if (uri.equals(getUri(Uris.PLAYLIST))) {
            notifyUriChange(getUri(Uris.FULL_ITEM));
        }

        if (uri.equals(getUri(Uris.IMAGE)) || uri.equals(getUri(Uris.ITEM)) || uri.equals(getUri(Uris.CHANNEL))) {
            notifyUriChange(getUri(Uris.FULL_CHANNEL));
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
