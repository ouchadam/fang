package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.CursorUtils;

import java.util.List;

class Playlist {

    static final long MISSING_ID = -1L;
    private static final int ZERO_INDEX_OFFSET = 1;

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
        return this.currentPosition == list.size();
    }

    public PlaylistItem get() {
        return list.get(currentPosition - ZERO_INDEX_OFFSET);
    }

    public boolean currentItemIdIsValid() {
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
        this.currentPosition = validatePosition(playlistPosition);
    }

    private int validatePosition(int playlistPosition) {
        return (playlistPosition - ZERO_INDEX_OFFSET) < list.size() && (playlistPosition - ZERO_INDEX_OFFSET) > 0 ? playlistPosition : ZERO_INDEX_OFFSET;
    }

    public void load() {
        Cursor cursor = getQuery(contentResolver);
        List<PlaylistItem> playlistItems = Collections.newArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlistItems.add(createPlaylistItem(cursor, itemSourceFetcher));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        list.clear();
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
        playlistItem.channel = cursorUtils.getString(Tables.Item.CHANNEL);
        playlistItem.title = cursorUtils.getString(Tables.Item.TITLE);
        playlistItem.imageUrl = getImageUrl(cursorUtils.getString(Tables.Item.HERO_IMAGE), cursorUtils.getString(Tables.ChannelImage.IMAGE_URL));

        int playPosition = cursorUtils.getInt(Tables.Playlist.PLAY_POSITION);
        int duration = cursorUtils.getInt(Tables.Playlist.MAX_DURATION);

        playlistItem.podcastPosition = new PodcastPosition(playPosition, duration);

        playlistItem.source = itemSourceFetcher.getSourceUri(playlistItem.downloadId);

        return playlistItem;
    }

    private String getImageUrl(String heroImage, String channelImage) {
        return heroImage == null ? channelImage : heroImage;
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public boolean isValid() {
        return !list.isEmpty();
    }

    public boolean currentItemIsValid() {
        PlaylistItem playlistItem = get();
        return playlistItem != null && playlistItem.source != null;
    }

    static class PlaylistItem {

        long id;
        int listPosition;
        PodcastPosition podcastPosition;
        long downloadId;
        Uri source;
        String channel;
        String imageUrl;
        String title;

        public boolean isDownloaded() {
            return source != null;
        }

    }

}
