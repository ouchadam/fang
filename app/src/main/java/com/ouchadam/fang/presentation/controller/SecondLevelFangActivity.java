package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.watcher.DownloadWatcher;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.AudioService;
import com.ouchadam.fang.audio.AudioServiceBinder;
import com.ouchadam.fang.audio.CompletionListener;
import com.ouchadam.fang.audio.OnStateSync;
import com.ouchadam.fang.audio.PlayingItemStateManager;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.FangBookKeeer;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.audio.event.PlayerEventInteractionManager;
import com.ouchadam.fang.presentation.item.ItemDownloader;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
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