package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.database.Cursor;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.CursorUtils;

import java.util.List;

class PlaylistLoader {

    private final ContentResolver contentResolver;
    private final ItemSourceFetcher itemSourceFetcher;

    public interface OnPlaylistLoad {
        void onPlaylistLoad(List<Playlist.PlaylistItem> playlistItems);
    }

    PlaylistLoader(ContentResolver contentResolver, ItemSourceFetcher itemSourceFetcher) {
        this.contentResolver = contentResolver;
        this.itemSourceFetcher = itemSourceFetcher;
    }

    public void load(final OnPlaylistLoad onPlaylistLoad) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<Playlist.PlaylistItem> playlistItems = getPlaylistItems();
                returnPayload(onPlaylistLoad, playlistItems);
            }
        }).start();
    }

    private List<Playlist.PlaylistItem> getPlaylistItems() {
        Cursor cursor = getQuery(contentResolver);
        List<Playlist.PlaylistItem> playlistItems = Collections.newArrayList();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                playlistItems.add(createPlaylistItem(cursor, itemSourceFetcher));
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        return playlistItems;
    }

    private synchronized void returnPayload(OnPlaylistLoad onPlaylistLoad, List<Playlist.PlaylistItem> playlistItems) {
        if (!playlistItems.isEmpty()) {
            onPlaylistLoad.onPlaylistLoad(playlistItems);
        }
    }

    private Cursor getQuery(ContentResolver contentResolver) {
        return contentResolver.query(
                FangProvider.getUri(Uris.FULL_ITEM),
                null,
                Tables.ItemPlay.DOWNLOAD_ID + "!=?",
                new String[]{"0"},
                " CAST (" + Tables.Playlist.LIST_POSITION + " AS DECIMAL)" + " ASC"
        );
    }

    private Playlist.PlaylistItem createPlaylistItem(Cursor cursor, ItemSourceFetcher itemSourceFetcher) {
        Playlist.PlaylistItem playlistItem = new Playlist.PlaylistItem();
        CursorUtils cursorUtils = new CursorUtils(cursor);

        playlistItem.id = cursorUtils.getLong(Tables.Playlist.ITEM_PLAYLIST);
        playlistItem.listPosition = cursorUtils.getInt(Tables.Playlist.LIST_POSITION);

        playlistItem.downloadId = cursorUtils.getLong(Tables.ItemPlay.DOWNLOAD_ID);
        playlistItem.channel = cursorUtils.getString(Tables.Item.ITEM_CHANNEL);
        playlistItem.title = cursorUtils.getString(Tables.Item.TITLE);
        playlistItem.imageUrl = getImageUrl(cursorUtils.getString(Tables.Item.HERO_IMAGE), cursorUtils.getString(Tables.ChannelImage.IMAGE_URL));

        int playPosition = cursorUtils.getInt(Tables.ItemPlay.PLAY_POSITION);
        int duration = cursorUtils.getInt(Tables.ItemPlay.MAX_DURATION);

        playlistItem.podcastPosition = new PodcastPosition(playPosition, duration);

        playlistItem.source = itemSourceFetcher.getSourceUri(playlistItem.downloadId);

        return playlistItem;
    }

    private String getImageUrl(String heroImage, String channelImage) {
        return heroImage == null ? channelImage : heroImage;
    }

}
