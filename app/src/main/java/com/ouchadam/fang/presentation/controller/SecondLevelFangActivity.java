package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

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
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.FangBookKeeer;
import com.ouchadam.fang.presentation.PlayerEvent;
import com.ouchadam.fang.presentation.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.drawer.DrawerNavigator;
import com.ouchadam.fang.presentation.drawer.FangDrawer;
import com.ouchadam.fang.presentation.panel.PlayerEventInteractionManager;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import com.ouchadam.fang.presentation.panel.SlidingPanelViewManipulator;

public abstract class SecondLevelFangActivity extends FragmentActivity implements ActionBarRefresher, ActionBarManipulator, SlidingPanelExposer, Downloader, SlidingPanelViewManipulator.OnSeekChanged {

    private SlidingPanelController slidingPanelController;
    private FangBookKeeer fangBookKeeer;
    private AudioServiceBinder audioServiceBinder;

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
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.second_level);
        fangBookKeeer = FangBookKeeer.newInstance(this);
        audioServiceBinder = new AudioServiceBinder(this, onStateSync, onCompletion);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initSlidingPaneController();
        initActionBar();
        startAudioService();
        onFangCreate(savedInstanceState);
    }

    private final CompletionListener onCompletion = new CompletionListener() {
        @Override
        public void onComplete() {
            slidingPanelController.hidePanel();
            PlayingItemStateManager playingItemStateManager = PlayingItemStateManager.from(SecondLevelFangActivity.this);
            playingItemStateManager.resetCurrentItem();
        }
    };

    private final OnStateSync onStateSync = new OnStateSync() {
        @Override
        public void onSync(SyncEvent syncEvent) {
            slidingPanelController.sync(syncEvent);
        }
    };

    private void initSlidingPaneController() {
        SlidingPanelViewManipulator slidingPanelViewManipulator = SlidingPanelViewManipulator.from(this, this, getRoot());
        PlayerEventInteractionManager playerEventManager = new PlayerEventInteractionManager(new PodcastPlayerEventBroadcaster(this));
        slidingPanelController = new SlidingPanelController(this, this, getSupportLoaderManager(), slidingPanelViewManipulator, playerEventManager);
    }

    private View getRoot() {
        return Views.findById(this, android.R.id.content);
    }

    private void initActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void startAudioService() {
        if (!audioServiceIsRunning()) {
            startService(new Intent(this, AudioService.class));
        }
    }

    private boolean audioServiceIsRunning() {
        return AndroidUtils.isServiceRunning(AudioService.class, this);
    }

    protected void onFangCreate(Bundle savedInstanceState) {
        // template
    }


    @Override
    public void setTitle(String title) {
        getActionBar().setTitle(title);
        refresh();
    }

    @Override
    public void refresh() {
        invalidateOptionsMenu();
    }

    @Override
    public void setData(long itemId) {
        slidingPanelController.setData(itemId);
    }

    @Override
    public boolean isActionBarShowing() {
        return getActionBar().isShowing();
    }

    @Override
    public void hideActionBar() {
        getActionBar().hide();
    }

    @Override
    public void showActionBar() {
        getActionBar().show();
    }

    @Override
    public void showExpanded() {
        slidingPanelController.showExpanded();
    }

    @Override
    public void showPanel() {
        slidingPanelController.showPanel();
    }

    @Override
    public long getId() {
        return slidingPanelController.getId();
    }

    @Override
    public void onBackPressed() {
        if (slidingPanelController.isShowing()) {
            slidingPanelController.close();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void watch(DownloadId downloadId, DownloadWatcher... downloadWatchers) {
        fangBookKeeer.watch(downloadId, downloadWatchers);
    }

    @Override
    public void store(DownloadId downloadId, long itemId) {
        fangBookKeeer.store(downloadId, itemId);
    }

    @Override
    public DownloadId keep(Downloadable downloadable) {
        return fangBookKeeer.keep(downloadable);
    }

    @Override
    public void restore(LazyWatcher lazyWatcher) {
        fangBookKeeer.restore(lazyWatcher);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        startAudioService();
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startAudioService();
        audioServiceBinder.bindService();
    }

    @Override
    protected void onStop() {
        super.onStop();
        audioServiceBinder.unbind();
        slidingPanelController.resetItem();
    }

    @Override
    public void onSeekChanged(PodcastPosition position) {
        new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().goTo(position));
    }

}