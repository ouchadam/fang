package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.topten.TopTenFragment;
import com.ouchadam.fang.presentation.topten.TopTenType;

public class TopTenActivity extends SecondLevelFangActivity {

    public static final String EXTRA_TOP_TEN_TYPE = "top_ten_type_extra";

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
            showDefaultFragment();
    }

    private void showDefaultFragment() {
        invalidateOptionsMenu();
        TopTenFragment topTenFragment = TopTenFragment.newInstance(getTopTenType());
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, topTenFragment).commit();
    }

    private TopTenType getTopTenType() {
        return TopTenType.valueOf(getIntent().getStringExtra(EXTRA_TOP_TEN_TYPE));
    }

}
