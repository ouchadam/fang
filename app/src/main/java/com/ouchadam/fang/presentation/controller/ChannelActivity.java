package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.SingleChannelFragment;

public class ChannelActivity extends FangActivity {

    public static final String EXTRA_CHANNEL = "channel_extra";

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
        initFragment();
    }

    private void initFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, SingleChannelFragment.newInstance(getChannelTitle())).commit();
    }

    private String getChannelTitle() {
        return getIntent().getStringExtra(EXTRA_CHANNEL);
    }
}
