package com.ouchadam.fang.presentation.controller;

import android.database.Cursor;
import com.ouchadam.fang.domain.item.ChannelItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import novoda.android.typewriter.cursor.CursorMarshaller;

public class ChannelItemMarshaller implements CursorMarshaller<ChannelItem> {
    @Override
    public ChannelItem marshall(Cursor cursor) {
        Item item = new ItemMarshaller().marshall(cursor);
        String channelImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Tables.ChannelImage.URL.name()));
        String channelTitle = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.CHANNEL.name()));
        return new ChannelItem(item, channelImageUrl, channelTitle);
    }

}
