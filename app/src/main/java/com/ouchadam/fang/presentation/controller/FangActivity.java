package com.ouchadam.fang.presentation.controller;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.bugsense.trace.BugSenseHandler;
import com.novoda.notils.caster.Views;
import com.novoda.notils.logger.Novogger;
import com.novoda.notils.meta.AndroidUtils;
import com.ouchadam.bookkeeper.DownloadWatcher;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.AudioService;
import com.ouchadam.fang.audio.AudioServiceBinder;
import com.ouchadam.fang.audio.CompletionListener;
import com.ouchadam.fang.audio.OnStateSync;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PlayerEventInteractionManager;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.presentation.FangBookKeeer;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.drawer.DrawerNavigator;
import com.ouchadam.fang.presentation.drawer.FangDrawer;
import com.ouchadam.fang.presentation.item.ActivityResultHandler;
import com.ouchadam.fang.presentation.item.ItemDownloader;
import com.ouchadam.fang.presentation.item.NavigatorForResult;
import com.ouchadam.fang.presentation.item.PlaylistFragment;
import com.ouchadam.fang.presentation.panel.OverflowCallback;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import com.ouchadam.fang.presentation.panel.SlidingPanelViewManipulator;
import com.ouchadam.fang.sync.FangSyncLifecycle;
import com.ouchadam.fang.sync.PullToRefreshExposer;

public abstract class FangActivity extends FragmentActivity implements ActionBarRefresher, ActionBarManipulator, SlidingPanelExposer, Downloader, SlidingPanelViewManipulator.OnSeekChanged, OverflowCallback, PullToRefreshExposer {

    private final FangSyncLifecycle fangSyncLifecycle;
    private FangDrawer fangDrawer;
    private SlidingPanelController slidingPanelController;
    private FangBookKeeer fangBookKeeer;
    private AudioServiceBinder audioServiceBinder;
    private BugsenseDelegate bugsense;

    public FangActivity() {
        this.fangSyncLifecycle = new FangSyncLifecycle();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            default:
                if (hasFangDrawer()) {
                    return fangDrawer.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
                }
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initBugsense();
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR_OVERLAY);
        setFangContentView();
        Novogger.enable(this);
        fangSyncLifecycle.init(this, this);
        fangBookKeeer = FangBookKeeer.getInstance(this);
        audioServiceBinder = new AudioServiceBinder(this, onStateSync, onCompletion);

        setVolumeControlStream(AudioManager.STREAM_MUSIC);

        fangInitActionBar();
        if (hasFangDrawer()) {
            initDrawer();
        }
        initSlidingPaneController();
        startAudioService();
        onFangCreate(savedInstanceState);
    }

    private void initBugsense() {
        bugsense = new BugsenseDelegate(getResources());
        bugsense.init(this);
    }

    protected boolean hasFangDrawer() {
        return true;
    }

    protected void setFangContentView() {
        setContentView(R.layout.drawer);
    }

    private final CompletionListener onCompletion = new CompletionListener() {
        @Override
        public void onComplete() {
            onDismissDrawer();
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
        DrawerNavigator drawerNavigator = new DrawerNavigator(getSupportFragmentManager());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, drawerNavigator.toArray());
        initDrawer(adapter, drawerNavigator);
    }

    protected void fangInitActionBar() {
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
            try {
                itemDownloader.downloadItem(fullItem.getItem());
            } catch (ItemDownloader.LinkValidator.BadLinkException e) {
                Toast.makeText(FangActivity.this, "Oops... " + e.getLink() + " is a bad url!", Toast.LENGTH_SHORT).show();
            }
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
        if (hasFangDrawer()) {
            fangDrawer.onPostCreate();
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (hasFangDrawer()) {
            fangDrawer.onConfigurationChanged(newConfig);
        }
    }

    @Override
    public void setTitle(String title) {
        getActionBar().setTitle(title);
        if (hasFangDrawer()) {
            fangDrawer.setOnCloseTitle(title);
        }
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
    public void onGoToDetails(FullItem fullItem) {
        showItemDetails(fullItem.getItemId(), getId());
    }

    private void showItemDetails(long itemId, long panelId) {
        new NavigatorForResult(this).toItemDetails(itemId, panelId);
    }

    @Override
    public void onRemove(FullItem fullItem) {
        Toast.makeText(this, "Todo", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDismissDrawer() {
        if (hasFangDrawer()) {
            fangDrawer.enable();
        }
        hidePanel();
    }

    @Override
    public boolean isPlaying(long itemId) {
        return slidingPanelController.isPlaying(itemId);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        startAudioService();
        ActivityResultHandler activityResultHandler = new ActivityResultHandler();
        activityResultHandler.handleResult(requestCode, resultCode, data, onResult);
    }

    private final ActivityResultHandler.OnResult onResult = new ActivityResultHandler.OnResult() {
        @Override
        public void onPlaySelected(long itemId, int playlistPosition, String playlist) {
            showPanel();
            setData(itemId);
            // TODO auto Expand or just play?
            PodcastPlayerEventBroadcaster broadcaster = new PodcastPlayerEventBroadcaster(FangActivity.this);
            broadcaster.broadcast(new PlayerEvent.Factory().newSource(playlistPosition, playlist));
            broadcaster.broadcast(new PlayerEvent.Factory().play());
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        fangSyncLifecycle.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fangSyncLifecycle.onPause(this);
    }

    @Override
    public void setRefreshing() {
        try {
            getPullToRefresh().setRefreshing();
        } catch (IllegalAccessException e) {
            Log.e("Tried to access pull to refresh but does not exist", e);
        }
    }

    @Override
    public void refreshComplete() {
        try {
            getPullToRefresh().refreshComplete();
        } catch (IllegalAccessException e) {
            Log.e("Tried to access pull to refresh but does not exist", e);
        }
    }

    private PullToRefreshExposer getPullToRefresh() throws IllegalAccessException {
        Fragment currentFragment = getCurrentFragment();
        if (currentFragment != null && currentFragment instanceof PullToRefreshExposer) {
            return (PullToRefreshExposer) currentFragment;
        }
        throw new IllegalAccessException("");
    }

    private Fragment getCurrentFragment() {
        return getSupportFragmentManager().findFragmentById(R.id.content_frame);
    }

}