package com.ouchadam.fang.audio;

import com.ouchadam.fang.domain.PodcastPosition;

public class SyncEvent {

    public final boolean isPlaying;
    public final PodcastPosition position;
    public final long itemId;

    private static final SyncEvent FRESH = new SyncEvent(false, PodcastPosition.idle(), -1L);

    public static SyncEvent fresh() {
        return FRESH;
    }

    public static SyncEvent idle(long itemId) {
        return new SyncEvent(false, PodcastPosition.idle(), itemId);
    }

    SyncEvent(boolean playing, PodcastPosition position, long longItemId) {
        isPlaying = playing;
        this.position = position;
        this.itemId = longItemId;
    }

    public boolean isFresh() {
        return position.isIdle();
    }

}
