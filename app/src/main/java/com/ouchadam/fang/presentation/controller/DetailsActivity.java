package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.item.DetailsFragment;
import com.ouchadam.fang.presentation.item.LatestFragment;

public class DetailsActivity extends FangActivity {
    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
        if (savedInstanceState != null) {
            showDefaultFragment(savedInstanceState.getLong("itemId"));
        }
    }

    private void showDefaultFragment(long itemId) {
        getActionBar().setTitle("Latest");
        invalidateOptionsMenu();
        DetailsFragment detailsFragment = DetailsFragment.newInstance(itemId);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, detailsFragment).commit();
    }
}
