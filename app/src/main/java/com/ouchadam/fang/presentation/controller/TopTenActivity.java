package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.topten.TopTenFragment;

public class TopTenActivity extends SecondLevelFangActivity {

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
            showDefaultFragment();
    }

    private void showDefaultFragment() {
        invalidateOptionsMenu();
        TopTenFragment topTenFragment = TopTenFragment.newInstance();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, topTenFragment).commit();
    }

}
