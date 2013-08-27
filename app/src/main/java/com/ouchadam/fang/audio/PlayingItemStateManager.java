package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.PositionPersister;

class PlayingItemStateManager {

    private final ContentResolver contentResolver;
    private final SharedPreferences preferences;

    public static PlayingItemStateManager from(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(AudioService.class.getSimpleName(), Context.MODE_PRIVATE);
        ContentResolver contentResolver = context.getContentResolver();
        return new PlayingItemStateManager(contentResolver, preferences);
    }

    PlayingItemStateManager(ContentResolver contentResolver, SharedPreferences preferences) {
        this.contentResolver = contentResolver;
        this.preferences = preferences;
    }

    public void persist(long itemId, PodcastPosition position) {
        if (itemId != PlayerHandler.MISSING_ID) {
            persistPosition(itemId, position);
            persistId(itemId);
        }
    }

    private void persistPosition(long itemId, PodcastPosition position) {
        new PositionPersister(itemId, contentResolver).persist(position);
    }

    private void persistId(long itemId) {
        preferences.edit().putLong("id", itemId).commit();
    }

    public long getStoredPlayingId() {
        return preferences.getLong("id", PlayerHandler.MISSING_ID);
    }
}
