package com.ouchadam.fang.audio;

import android.content.Context;
import android.net.Uri;

import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;

public class PlayItem {

    private final long id;
    private final Uri source;
    private final int playlistPosition;

    public static PlayItem from(FullItem item, Context context) {
        long itemId = item.getItemId();
        Uri sourceUri = ItemSourceFetcher.from(context).getSourceUri(item.getDownloadId());
        int playlistPosition = item.getPlaylistPosition();
        return new PlayItem(itemId, sourceUri, playlistPosition);
    }

    public PlayItem(long id, Uri source, int playlistPosition) {
        this.id = id;
        this.source = source;
        this.playlistPosition = playlistPosition;
    }

    public boolean isValid() {
        return source != null && id != Playlist.MISSING_ID;
    }

    public long getId() {
        return id;
    }

    public Uri getSource() {
        return source;
    }

    public int getPlaylistPosition() {
        return playlistPosition;
    }
}
