package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.view.MenuItem;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import com.ouchadam.fang.presentation.panel.SlidingPanelViewManipulator;

public abstract class SecondLevelFangActivity extends FangActivity implements ActionBarRefresher, ActionBarManipulator, SlidingPanelExposer, Downloader, SlidingPanelViewManipulator.OnSeekChanged {

    @Override
    protected void setFangContentView() {
        setContentView(R.layout.second_level);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return  super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected boolean hasFangDrawer() {
        return false;
    }

    @Override
    protected void fangInitActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

}