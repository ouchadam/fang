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
import android.widget.Toast;

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
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.FangBookKeeer;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.drawer.DrawerNavigator;
import com.ouchadam.fang.presentation.drawer.FangDrawer;
import com.ouchadam.fang.presentation.item.ItemDownloader;
import com.ouchadam.fang.presentation.item.Navigator;
import com.ouchadam.fang.presentation.panel.OverflowCallback;
import com.ouchadam.fang.audio.event.PlayerEventInteractionManager;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import com.ouchadam.fang.presentation.panel.SlidingPanelViewManipulator;

public abstract class FangActivity extends FragmentActivity implements ActionBarRefresher, ActionBarManipulator, SlidingPanelExposer, Downloader, SlidingPanelViewManipulator.OnSeekChanged, OverflowCallback {

    private FangDrawer fangDrawer;
    private SlidingPanelController slidingPanelController;
    private FangBookKeeer fangBookKeeer;
    private AudioServiceBinder audioServiceBinder;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                return fangDrawer.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
        }
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setContentView(R.layout.drawer);
        fangBookKeeer = FangBookKeeer.getInstance(this);
        audioServiceBinder = new AudioServiceBinder(this, onStateSync, onCompletion);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        initDrawer();
        initSlidingPaneController();
        startAudioService();
        onFangCreate(savedInstanceState);
    }

    private final CompletionListener onCompletion = new CompletionListener() {
        @Override
        public void onComplete() {
            hidePanel();
        }
    };

    private void hidePanel() {
        // TODO possibly do nothing?
        slidingPanelController.close();
        slidingPanelController.hidePanel();
        new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().reset());
    }

    private final OnStateSync onStateSync = new OnStateSync() {
        @Override
        public void onSync(SyncEvent syncEvent) {
            slidingPanelController.sync(syncEvent);
        }
    };

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
        fangDrawer.setOnCloseTitle(getTitle().toString());
    }

    private void initSlidingPaneController() {
        SlidingPanelViewManipulator slidingPanelViewManipulator = SlidingPanelViewManipulator.from(this, this, getRoot(), fangDrawer, onDownload, this);
        PlayerEventInteractionManager playerEventManager = new PlayerEventInteractionManager(new PodcastPlayerEventBroadcaster(this));
        slidingPanelController = new SlidingPanelController(this, getSupportLoaderManager(), slidingPanelViewManipulator, playerEventManager);
    }

    private final SlidingPanelViewManipulator.OnDownloadClickListener onDownload = new SlidingPanelViewManipulator.OnDownloadClickListener() {
        @Override
        public void onDownloadClicked(FullItem fullItem) {
            ItemDownloader itemDownloader = new ItemDownloader(FangActivity.this, FangActivity.this);
            itemDownloader.downloadItem(fullItem.getItem());
        }
    };

    private View getRoot() {
        return Views.findById(this, android.R.id.content);
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
    public void setTitle(String title) {
        getActionBar().setTitle(title);
        fangDrawer.setOnCloseTitle(title);
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

    @Override
    public void onMarkHeard(FullItem fullItem) {
        new PodcastPlayerEventBroadcaster(this).broadcast(new PlayerEvent.Factory().goTo(fullItem.getInitialPlayPosition().asCompleted()));
    }

    @Override
    public void onGoToChannel(FullItem fullItem) {
        new Navigator(this).toChannel(fullItem.getChannelTitle());
    }

    @Override
    public void onRemove(FullItem fullItem) {
        Toast.makeText(this, "Todo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismissDrawer(FullItem fullItem) {
        fangDrawer.enable();
        hidePanel();
    }
}