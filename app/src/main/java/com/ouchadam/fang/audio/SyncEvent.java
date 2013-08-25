package com.ouchadam.fang.audio;

public class SyncEvent {

    public final boolean isPlaying;
    public final PodcastPosition position;
    public final long itemId;

    private static final SyncEvent IDLE = new SyncEvent(false, PodcastPosition.idle(), -1L);

    public static SyncEvent idle() {
        return IDLE;
    }

    SyncEvent(boolean playing, PodcastPosition position, long longItemId) {
        isPlaying = playing;
        this.position = position;
        this.itemId = longItemId;
    }

    public boolean isIdle() {
        return equals(IDLE);
    }
}
