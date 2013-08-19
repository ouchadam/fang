package com.ouchadam.fang.presentation.controller;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.SingleChannelFragment;

public class ChannelActivity extends FragmentActivity {

    public static final String EXTRA_CHANNEL = "channel_extra";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.channel_container, SingleChannelFragment.newInstance(getChannelTitle())).commit();
    }

    private String getChannelTitle() {
        return getIntent().getStringExtra(EXTRA_CHANNEL);
    }
}
