package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

public class ActivityResultHandler {

    public static final int NEW_PLAY_RESULT = 7;

    private static final String ITEM_ID = "itemId";
    private static final String ITEM_SOURCE = "itemSource";

    public interface OnResult {
        void onPlaySelected(long itemId, Uri itemSource);
    }

    public void handleResult(int requestCode, int resultCode, Intent data, OnResult onResult) {
        if (requestCode == NEW_PLAY_RESULT && resultCode == Activity.RESULT_OK) {
            long itemId = data.getLongExtra(ITEM_ID, -1);
            Uri itemSource = data.getParcelableExtra(ITEM_SOURCE);
            onResult.onPlaySelected(itemId, itemSource);
        }
    }

    public void returnWithPlayingItem(Activity activity, long itemId, Uri source) {
        Intent intent = new Intent();
        intent.putExtra(ITEM_ID, itemId);
        intent.putExtra(ITEM_SOURCE, source);
        activity.setResult(Activity.RESULT_OK, intent);
        activity.finish();
    }

}
