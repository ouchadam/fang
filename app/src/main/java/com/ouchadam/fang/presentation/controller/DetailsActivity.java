package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.item.DetailsFragment;

public class DetailsActivity extends FangActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        initContent();
    }

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
        initActionBar();
        initContent();
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        showDrawerIndicator(false);
    }

    private void initContent() {
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
