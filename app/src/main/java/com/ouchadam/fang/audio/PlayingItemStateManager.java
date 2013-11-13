package com.ouchadam.fang.audio;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.PositionPersister;

public class PlayingItemStateManager {

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

    public void persist(long itemId, PodcastPosition position, Uri source) {
        if (itemId != Playlist.MISSING_ID) {
            persistPosition(itemId, position);
            persistId(itemId);
            persistSource(source);
        }
    }

    private void persistPosition(long itemId, PodcastPosition position) {
        new PositionPersister(itemId, contentResolver).persist(position);
    }

    private void persistSource(Uri uri) {
        preferences.edit().putString("source", uri.toString()).apply();
    }

    private void persistId(long itemId) {
        preferences.edit().putLong("id", itemId).apply();
    }

    public Playlist.PlayItem getStoredItem() {
        Playlist.PlayItem playItem = new Playlist.PlayItem();
        playItem.id = getStoredPlayingId();
        playItem.source = getSource();
        return playItem;
    }

    private long getStoredPlayingId() {
        return preferences.getLong("id", Playlist.MISSING_ID);
    }

    private Uri getSource() {
        String source = preferences.getString("source", "");
        if (!source.isEmpty()) {
            return Uri.parse(source);
        }
        return null;
    }

    public void resetCurrentItem() {
        preferences.edit().remove("id").apply();
    }
}
