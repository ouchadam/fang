package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.CursorUtils;

import java.util.List;

class Playlist {

    static final long MISSING_ID = -1L;

    private final List<PlaylistItem> list;
    private final ItemSourceFetcher itemSourceFetcher;
    private final ContentResolver contentResolver;

    private long playingItemId;
    private int currentPosition;

    public static Playlist from(Context context) {
        return new Playlist(ItemSourceFetcher.from(context), context.getContentResolver());
    }

    public Playlist(ItemSourceFetcher itemSourceFetcher, ContentResolver contentResolver) {
        this.itemSourceFetcher = itemSourceFetcher;
        this.contentResolver = contentResolver;
        this.list = Collections.newArrayList();
        this.playingItemId = MISSING_ID;
    }

    public boolean isLast() {
        return list.size() == 1 || list.isEmpty();
    }

    public PlaylistItem get() {
        return list.get(currentPosition - 1);
    }

    public boolean currentItemIsValid() {
        return playingItemId != MISSING_ID;
    }

    public void resetCurrent() {
        playingItemId = MISSING_ID;
    }

    public void setCurrent(long itemId) {
        this.playingItemId = itemId;
    }

    public long getCurrentId() {
        return playingItemId;
    }

    public void moveToNext() {
        moveTo(currentPosition + 1);
    }

    public boolean hasNext() {
        return !isLast();
    }

    public void moveTo(int playlistPosition) {
        this.currentPosition = playlistPosition;
    }

    public void load() {
        Cursor cursor = getQuery(contentResolver);
        List<PlaylistItem> playlistItems = Collections.newArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlistItems.add(createPlaylistItem(cursor, itemSourceFetcher));
            } while (cursor.moveToNext());
            cursor.close();
        }
        list.addAll(playlistItems);
    }

    private Cursor getQuery(ContentResolver contentResolver) {
        return contentResolver.query(
                FangProvider.getUri(Uris.FULL_ITEM),
                null,
                Tables.Playlist.DOWNLOAD_ID + "!=?",
                new String[]{"0"},
                " CAST (" + Tables.Playlist.LIST_POSITION + " AS DECIMAL)" + " ASC"
        );
    }

    private PlaylistItem createPlaylistItem(Cursor cursor, ItemSourceFetcher itemSourceFetcher) {
        PlaylistItem playlistItem = new PlaylistItem();
        CursorUtils cursorUtils = new CursorUtils(cursor);

        playlistItem.id = cursorUtils.getLong(Tables.Playlist.ITEM_ID);
        playlistItem.listPosition = cursorUtils.getInt(Tables.Playlist.LIST_POSITION);

        playlistItem.downloadId = cursorUtils.getLong(Tables.Playlist.DOWNLOAD_ID);

        int playPosition = cursorUtils.getInt(Tables.Playlist.PLAY_POSITION);
        int duration = cursorUtils.getInt(Tables.Playlist.MAX_DURATION);

        playlistItem.podcastPosition = new PodcastPosition(playPosition, duration);

        playlistItem.source = itemSourceFetcher.getSourceUri(playlistItem.downloadId);

        Log.e("!!!", "Adding to playlist : " + cursorUtils.getString(Tables.Channel.CHANNEL_TITLE));

        return playlistItem;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    static class PlaylistItem {

        long id;
        int listPosition;
        PodcastPosition podcastPosition;
        long downloadId;
        Uri source;

        public boolean isDownloaded() {
            return source != null;
        }

    }

}
