package com.ouchadam.fang.audio;

import android.net.Uri;

import com.ouchadam.fang.domain.PodcastPosition;

import java.util.List;

class Playlist {

    static final long MISSING_ID = -1L;

    private long playingItemId;

    List<PlayItem> list;

    public Playlist() {
        this.playingItemId = MISSING_ID;
    }

    public boolean isLast() {
        return list.size() == 1;
    }

    public void add(long itemId) {

    }

    public PlayItem get() {
        return list.get(0);
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
        list.remove(0);
    }

    public boolean hasNext() {
        return !isLast();
    }

    static class PlayItem {
        Uri source;
        long id;
        PodcastPosition position;

        public boolean isValid() {
            return source != null && id != MISSING_ID;
        }
    }
}
