package com.ouchadam.fang.presentation;

import android.database.Cursor;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.FangDuration;
import com.ouchadam.fang.domain.item.Audio;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class ItemMarshaller implements CursorMarshaller<Item> {
    @Override
    public Item marshall(Cursor cursor) {
        String title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.TITLE.name()));
        String summary = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.SUMMARY.name()));
        String subtitle = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.SUBTITLE.name()));
        String heroImage = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.HERO_IMAGE.name()));
        int columnId = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.Item._id.name()));
        FangCalendar pubDate = new FangCalendar(cursor.getLong(cursor.getColumnIndexOrThrow(Tables.Item.PUBDATE.name())));
        FangDuration duration = new FangDuration(cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.DURATION.name())));

        Audio audio = createAudio(cursor);

        return new Item(title, "", heroImage, pubDate, duration, audio, subtitle, summary, columnId);
    }

    private Audio createAudio(Cursor cursor) {
        String audioType = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.AUDIO_TYPE.name()));
        String audioUrl = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.AUDIO_URL.name()));

        return new Audio(audioUrl, audioType);
    }
}
