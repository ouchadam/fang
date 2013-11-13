package com.ouchadam.fang.audio;

import com.novoda.notils.java.Collections;

import java.util.List;

class Playlist {

    static final long MISSING_ID = -1L;

    private final List<PlayItem> list;
    private long playingItemId;

    public Playlist() {
        this.list = Collections.newArrayList();
        this.playingItemId = MISSING_ID;
    }

    public boolean isLast() {
        return list.size() == 1 || list.isEmpty();
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

}
