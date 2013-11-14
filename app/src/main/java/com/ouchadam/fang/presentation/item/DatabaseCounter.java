package com.ouchadam.fang.presentation.item;

import android.content.ContentResolver;
import android.database.Cursor;

import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.database.Uris;

public class DatabaseCounter {

    private static final int ZERO_COUNT = 0;
    private final ContentResolver contentResolver;
    private final Uris uri;
    private final String[] projection;
    private final String selection;
    private final String[] selectionArgs;

    public DatabaseCounter(ContentResolver contentResolver, Uris uri, String[] projection, String selection, String[] selectionArgs) {
        this.contentResolver = contentResolver;
        this.uri = uri;
        this.projection = projection;
        this.selection = selection;
        this.selectionArgs = selectionArgs;
    }

    public int getCurrentCount() {
        Cursor cursor = getQuery();
        try {
            int count = ZERO_COUNT;
            if (isValid(cursor)) {
                count = cursor.getCount();
            }
            return count;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private Cursor getQuery() {
        return contentResolver.query(
                FangProvider.getUri(uri),
                projection,
                selection,
                selectionArgs,
                null
        );
    }

    private boolean isValid(Cursor cursor) {
        return cursor != null && cursor.moveToFirst();
    }
}
