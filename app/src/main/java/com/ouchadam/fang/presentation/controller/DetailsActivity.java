package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.item.DetailsFragment;
import com.ouchadam.fang.presentation.item.LatestFragment;

public class DetailsActivity extends FangActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        init();
    }

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        showDrawerIndicator(false);
        init();
    }

    private void init() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            showDefaultFragment(extras.getLong("itemId"));
        }
    }

    private void showDefaultFragment(long itemId) {
        invalidateOptionsMenu();
        DetailsFragment detailsFragment = DetailsFragment.newInstance(itemId);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, detailsFragment).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle extras = getIntent().getExtras();
        if (extras != null && extras.getLong("playingItemId", -1) != -1) {
            initPanel(extras.getLong("playingItemId"));
        }
    }

    private void initPanel(long playingItemId) {
        showPanel();
        setData(playingItemId);
    }
}
