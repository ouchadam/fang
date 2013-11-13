package com.ouchadam.fang.presentation;

import android.database.Cursor;

public class CursorUtils {

    private final Cursor cursor;

    public CursorUtils(Cursor cursor) {
        this.cursor = cursor;
    }

    public String getString(Enum column) {
        return cursor.getString(getColumnIndex(column));
    }

    public int getInt(Enum column) {
        return cursor.getInt(getColumnIndex(column));
    }

    public long getLong(Enum column) {
        return cursor.getLong(getColumnIndex(column));
    }

    private int getColumnIndex(Enum column) {
        return cursor.getColumnIndexOrThrow(column.name());
    }

    public boolean getBoolean(Enum column) {
        return getInt(column) == 1;
    }
}
