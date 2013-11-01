package com.ouchadam.fang.presentation.controller;

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
import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.AudioService;
import com.ouchadam.fang.audio.AudioServiceBinder;
import com.ouchadam.fang.notification.FangNotification;
import com.ouchadam.fang.notification.NotificationService;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.FangBookKeeer;
import com.ouchadam.fang.presentation.PlayerEvent;
import com.ouchadam.fang.presentation.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.drawer.DrawerNavigator;
import com.ouchadam.fang.presentation.drawer.FangDrawer;
import com.ouchadam.fang.presentation.panel.SlidingPanelViewManipulator;

public abstract class FangActivity extends FragmentActivity implements ActionBarRefresher, ActionBarManipulator, SlidingPanelExposer, Downloader, SlidingPanelViewManipulator.OnSeekChanged {

    private FangDrawer fangDrawer;
    private SlidingPanelController slidingPanelController;
    private FangBookKeeer fangBookKeeer;
    private AudioServiceBinder audioServiceBinder;

    private boolean isPlaying = false;
    private long itemId = -1L;

    private FangNotification fangNotification;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (fangDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.drawer);
        fangBookKeeer = FangBookKeeer.newInstance(this);
        fangNotification = FangNotification.from(this);
        audioServiceBinder = new AudioServiceBinder(this, onStateSync);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initSlidingPaneController();
        initDrawer();
        startAudioService();
        onFangCreate(savedInstanceState);
    }

    private final AudioServiceBinder.OnStateSync onStateSync = new AudioServiceBinder.OnStateSync() {
        @Override
        public void onSync(SyncEvent syncEvent) {
            FangActivity.this.isPlaying = syncEvent.isPlaying;
            FangActivity.this.itemId = syncEvent.itemId;
            slidingPanelController.sync(syncEvent);
        }
    };

    private void initSlidingPaneController() {
        SlidingPanelViewManipulator slidingPanelViewManipulator = SlidingPanelViewManipulator.from(this, this, getRoot());
        Broadcaster<PlayerEvent> playerEventBroadcaster = new PodcastPlayerEventBroadcaster(this);
        slidingPanelController = new SlidingPanelController(this, this, getSupportLoaderManager(), slidingPanelViewManipulator, playerEventBroadcaster);
    }

    private View getRoot() {
        return Views.findById(this, android.R.id.content);
    }

    private void initDrawer() {
        initActionBar();
        DrawerNavigator drawerNavigator = new DrawerNavigator(getSupportFragmentManager());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerNavigator.toArray());
        initDrawer(adapter, drawerNavigator);
    }

    private void initActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);
    }

    private void initDrawer(ListAdapter listAdapter, DrawerNavigator drawerNavigator) {
        fangDrawer = FangDrawer.newInstance(this, drawerNavigator);
        fangDrawer.setAdapter(listAdapter);
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
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fangDrawer.onPostCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fangDrawer.onConfigurationChanged(newConfig);
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
    public void show() {
        slidingPanelController.show();
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
    protected void onResume() {
        super.onResume();
        startAudioService();
        audioServiceBinder.bindService();
        fangNotification.dismiss();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!isPlaying) {
            new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().stop());
        }
        audioServiceBinder.unbind();
        showNotification(itemId);
        slidingPanelController.resetItem();
    }

    private void showNotification(long itemId) {
        if (isPlaying) {
            NotificationService.start(this, itemId, isPlaying);
        }
    }

    @Override
    public void onSeekChanged(PodcastPosition position) {
        new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().goTo(position));
    }

}