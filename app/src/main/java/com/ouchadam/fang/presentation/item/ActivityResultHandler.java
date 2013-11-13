package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.content.Intent;

public class ActivityResultHandler {

    public static final int NEW_PLAY_RESULT = 7;

    private static final String PLAYLIST_POSITION = "playlistPosition";
    private static final String PLAYLIST_NAME = "playlist";
    private static final String ITEM_ID = "id";

    public interface OnResult {
        void onPlaySelected(long itemId, int playlistPosition, String playlist);
    }

    public void handleResult(int requestCode, int resultCode, Intent data, OnResult onResult) {
        if (requestCode == NEW_PLAY_RESULT && resultCode == Activity.RESULT_OK) {
            int playlistPosition = data.getIntExtra(PLAYLIST_POSITION, -1);
            long itemId = data.getLongExtra(ITEM_ID, -1L);
            String playlistName = data.getStringExtra(PLAYLIST_NAME);
            onResult.onPlaySelected(itemId, playlistPosition, playlistName);
        }
    }

    public void returnWithPlayingItem(Activity activity, long itemId, int playlistPosition, String playlist) {
        Intent intent = new Intent();
        intent.putExtra(PLAYLIST_POSITION, playlistPosition);
        intent.putExtra(ITEM_ID, itemId);
        intent.putExtra(PLAYLIST_NAME, playlist);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

}
