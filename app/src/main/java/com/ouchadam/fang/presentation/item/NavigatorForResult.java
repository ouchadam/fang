package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.content.Intent;

import com.ouchadam.fang.presentation.controller.DetailsActivity;

public class NavigatorForResult {

    private final Activity activity;

    public NavigatorForResult(Activity activity) {
        this.activity = activity;
    }

    public void toItemDetails(long toItemId, long currentSelectedId) {
        Intent intent = new Intent(activity, DetailsActivity.class);
        intent.putExtra("itemId", toItemId);
        if (currentSelectedId != -1) {
            intent.putExtra("playingItemId", currentSelectedId);
        }
        activity.startActivityForResult(intent, ActivityResultHandler.NEW_PLAY_RESULT);
    }
}
