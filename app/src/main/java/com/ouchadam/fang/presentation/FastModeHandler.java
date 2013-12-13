package com.ouchadam.fang.presentation;

import android.app.Activity;
import android.content.Context;

import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.presentation.item.ActivityCallback;
import com.ouchadam.fang.presentation.item.ItemDownloader;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

public class FastModeHandler implements ActivityCallback {

    private Context context;
    private SlidingPanelExposer panelController;
    private PodcastPlayerEventBroadcaster eventBroadcaster;
    private Downloader downloader;
    private FastModeEnabler fastModeEnabler;

    @Override
    public void onAttach(Activity activity) {
        this.context = activity;
        this.panelController = Classes.from(activity);
        this.downloader = Classes.from(activity);
        this.eventBroadcaster = new PodcastPlayerEventBroadcaster(activity);
        this.fastModeEnabler = FastModeEnabler.from(context);
    }

    public void onFastMode(FullItem fullItem, LazyWatcher lazyWatcher) {
        if (fullItem.isDownloaded()) {
            play(fullItem);
        } else {
            download(fullItem, lazyWatcher);
        }
    }

    private void download(FullItem item, LazyWatcher lazyWatcher) {
        try {
            ItemDownloader itemDownloader = new ItemDownloader(downloader, context);
            itemDownloader.setWatchers(lazyWatcher);
            itemDownloader.downloadItem(item.getItem());
        } catch (ItemDownloader.LinkValidator.BadLinkException e) {
            Log.d("Oops... \" + e.getLink() + \" is a bad url!");
        }
    }

    private void play(FullItem item) {
        if (panelController.getId() == item.getItemId()) {
            eventBroadcaster.broadcast(new PlayerEvent.Factory().playPause());
        } else {
            eventBroadcaster.broadcast(new PlayerEvent.Factory().pause());
            eventBroadcaster.broadcast(new PlayerEvent.Factory().newSource(item.getPlaylistPosition(), "PLAYLIST"));
            eventBroadcaster.broadcast(new PlayerEvent.Factory().play());
        }
    }

    public boolean isPlaying(long itemId) {
        return panelController.isPlaying(itemId);
    }

    public boolean isEnabled() {
        return fastModeEnabler.isEnabled();
    }
}
