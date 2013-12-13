package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;

import java.util.List;

public class Playlist {

    static final long MISSING_ID = -1L;
    private static final int ZERO_INDEX_OFFSET = 1;

    private final List<PlaylistItem> list;
    private final PlaylistLoader playlistLoader;

    private long playingItemId;
    private int currentPosition;
    private OnPlaylistPrepared onPlaylistPrepared;

    public interface OnPlaylistPrepared {
        void onPrepared();
    }

    public static Playlist from(Context context) {
        return new Playlist(new PlaylistLoader(context.getContentResolver(), ItemSourceFetcher.from(context)));
    }

    public Playlist(PlaylistLoader playlistLoader) {
        this.playlistLoader = playlistLoader;
        this.list = Collections.newArrayList();
        this.playingItemId = MISSING_ID;
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

    private boolean isLast() {
        return this.currentPosition == list.size();
    }

    public void moveTo(int playlistPosition) {
        this.currentPosition = validatePosition(playlistPosition);
    }

    private int validatePosition(int playlistPosition) {
        return (playlistPosition - ZERO_INDEX_OFFSET) < list.size() && (playlistPosition - ZERO_INDEX_OFFSET) > 0 ? playlistPosition : ZERO_INDEX_OFFSET;
    }

    public void refresh(ContentResolver contentResolver) {
        new PlaylistPositionRefresher(contentResolver, playlistLoader).refresh();
    }

    public void load(OnPlaylistPrepared onPlaylistPrepared) {
        this.onPlaylistPrepared = onPlaylistPrepared;
        playlistLoader.load(onPlaylistLoad);
    }

    private final PlaylistLoader.OnPlaylistLoad onPlaylistLoad = new PlaylistLoader.OnPlaylistLoad() {
        @Override
        public void onPlaylistLoad(List<PlaylistItem> playlistItems) {
            handleNewPlaylist(playlistItems);
        }
    };

    private void handleNewPlaylist(List<PlaylistItem> playlistItems) {
        list.clear();
        list.addAll(playlistItems);
        if (onPlaylistPrepared != null) {
            onPlaylistPrepared.onPrepared();
        } else {
            Log.e("onPlaylistPrepared listener is null, skipping callback");
        }
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

    public boolean hasPosition(int position) {
        return list.size() >= position && position > 0;
    }

    public static class PlaylistItem {

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

        public void setListPosition(int listPosition) {
            this.listPosition = listPosition;
        }

    }

}
