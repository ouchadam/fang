package com.ouchadam.fang.presentation.controller;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.github.frankiesardo.icepick.annotation.Icicle;
import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.caster.Fragments;
import com.ouchadam.fang.R;
import com.ouchadam.fang.debug.DebugActivity;
import com.ouchadam.fang.debug.FeedServiceInfo;
import com.ouchadam.fang.setting.SettingsActivity;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class FragmentControllerActivity extends FangActivity {

    @Icicle
    public String activityTitle;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.debug, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_debug:
                onDebugClicked();
                break;

            case R.id.ab_settings:
                onSettingsClicked();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void onDebugClicked() {
        startActivity(new Intent(this, DebugActivity.class));
    }

    private void onSettingsClicked() {
        Intent settingsIntent = new Intent(this, SettingsActivity.class);
        startActivity(settingsIntent);
    }

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        Bundles.restoreInstanceState(this, savedInstanceState);
        initActionBar();
        if (hasEmptyContent()) {
            showDefaultFragment();
        }
    }

    private void initActionBar() {
        if (activityTitle != null) {
            getActionBar().setTitle(activityTitle);
        }
        invalidateOptionsMenu();
    }

    private boolean hasEmptyContent() {
        return Fragments.findFragmentById(getSupportFragmentManager(), R.id.content_frame) == null;
    }

    private void showDefaultFragment() {
        Fragment defaultFragment = DefaultFragmentManager.from(this).getDefaultFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, defaultFragment).commit();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getActionBar() != null && getActionBar().getTitle() != null) {
            activityTitle = getActionBar().getTitle().toString();
        }
        Bundles.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

}
