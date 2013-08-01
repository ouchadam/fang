package com.ouchadam.fang.presentation.controller;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.TextView;
import com.github.frankiesardo.icepick.annotation.Icicle;
import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.android.Fragments;
import com.novoda.notils.android.Views;
import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.debug.DebugActivity;
import com.ouchadam.fang.presentation.item.LatestFragment;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

import java.io.File;
import java.net.URL;

public class MyActivity extends DrawerActivity {

    @Icicle
    public String activityTitle;
    private BookKeeper bookKeeper;

    private Downloadable downloadable;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.debug, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.ab_debug) {
            startActivity(new Intent(this, DebugActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        Bundles.restoreInstanceState(this, savedInstanceState);
        if (activityTitle != null) {
            getActionBar().setTitle(activityTitle);
        }
        invalidateOptionsMenu();
        if (hasEmptyContent()) {
            showDefaultFragment();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        initBookKeeper();
    }

    private void initBookKeeper() {
        bookKeeper = new BookKeeper(this);
        if (bookKeeper.serviceIsRunning() && hasDownload()) {
            bookKeeper.attachWatchers(downloadable, getWatchers());
        }
    }

    private boolean hasDownload() {
        return downloadable != null;
    }

    private DownloadWatcher[] getWatchers() {
        DownloadWatcher[] downloadWatchers = new DownloadWatcher[3];
        downloadWatchers[0] = new NotificationWatcher(this);
        return downloadWatchers;
    }

    private boolean hasEmptyContent() {
        return Fragments.findFragmentById(getSupportFragmentManager(), R.id.content_frame) == null;
    }

    private void showDefaultFragment() {
        getActionBar().setTitle("Latest");
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new LatestFragment()).commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (getActionBar().getTitle() != null) {
            activityTitle = getActionBar().getTitle().toString();
        }
        Bundles.saveInstanceState(this, outState);
        super.onSaveInstanceState(outState);
    }

}
