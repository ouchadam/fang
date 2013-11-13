package com.ouchadam.fang.audio;

import android.content.Context;
import android.net.Uri;

import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;

public class PlayItem {

    public static PlayItem from(FullItem item, Context context) {
        PlayItem playItem = new PlayItem();
        playItem.id = item.getItemId();
        playItem.source = ItemSourceFetcher.from(context).getSourceUri(item);
        playItem.position = item.getInitialPlayPosition();
        return playItem;
    }

    Uri source;
    long id;
    PodcastPosition position;

    public boolean isValid() {
        return source != null && id != Playlist.MISSING_ID;
    }

    public long getId() {
        return id;
    }

    public Uri getSource() {
        return source;
    }

    public PodcastPosition getPosition() {
        return position;
    }
}
