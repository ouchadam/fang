package com.ouchadam.fang.presentation.controller;

import android.database.Cursor;
import com.ouchadam.fang.domain.LocalItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import novoda.android.typewriter.cursor.CursorMarshaller;

public class LocalItemMarshaller implements CursorMarshaller<LocalItem> {

    @Override
    public LocalItem marshall(Cursor cursor) {
        Item item = new ItemMarshaller().marshall(cursor);
        long downloadId = cursor.getLong(cursor.getColumnIndexOrThrow(Tables.Playlist.DOWNLOAD_ID.name()));
        return new LocalItem(item, downloadId);
    }

}
