package com.ouchadam.fang.presentation.panel;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.util.Log;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.audio.PlayItem;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PlayerEventInteractionManager;

public class SlidingPanelController implements SlidingPanelExposer {

    private final Context context;
    private final LoaderManager loaderManager;
    private final SlidingPanelViewManipulator slidingPanelViewManipulator;
    private final PlayerEventInteractionManager playerEventInteractionManager;

    private ItemQueryer itemQueryer;

    public SlidingPanelController(Context context, LoaderManager loaderManager, SlidingPanelViewManipulator slidingPanelViewManipulator, PlayerEventInteractionManager playerEventInteractionManager) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.slidingPanelViewManipulator = slidingPanelViewManipulator;
        this.playerEventInteractionManager = playerEventInteractionManager;
    }

    @Override
    public void setData(long itemId) {
        if (itemQueryer != null) {
            itemQueryer.stop();
        }
        itemQueryer = new ItemQueryer(context, itemId, loaderManager, onItem);
        itemQueryer.query();
    }

    private final ItemQueryer.OnItemListener onItem = new ItemQueryer.OnItemListener() {
        @Override
        public void onItem(FullItem item) {
            if (item.isDownloaded()) {
                PlayItem playItem = PlayItem.from(item, context);
                PlayerEvent sourceEvent = new PlayerEvent.Factory().newSource(playItem);
                PlayerEvent gotoEvent = new PlayerEvent.Factory().goTo(item.getInitialPlayPosition());
                playerEventInteractionManager.setData(sourceEvent, gotoEvent);
            }
            initialiseViews(item);
        }
    };

    private void initialiseViews(final FullItem item) {
        slidingPanelViewManipulator.fromItem(item);
        slidingPanelViewManipulator.setMediaClickedListener(onMediaClicked);
    }

    private final MediaClickManager.OnMediaClickListener onMediaClicked = new MediaClickManager.OnMediaClickListener() {
        @Override
        public void onMediaClicked(MediaClickManager.MediaPressed mediaPressed) {
            if (mediaPressed == MediaClickManager.MediaPressed.PLAY) {
                playerEventInteractionManager.play(slidingPanelViewManipulator.getPosition());
            } else {
                playerEventInteractionManager.pause();
            }
        }
    };

    @Override
    public void showExpanded() {
        slidingPanelViewManipulator.expand();
    }

    @Override
    public long getId() {
        if (itemQueryer != null) {
            return itemQueryer.getId();
        }
        return -1;
    }

    @Override
    public void showPanel() {
        slidingPanelViewManipulator.show();
    }

    public void hidePanel() {
        resetItem();
        slidingPanelViewManipulator.hide();
    }

    public void close() {
        slidingPanelViewManipulator.close();
    }

    public boolean isShowing() {
        return slidingPanelViewManipulator.isShowing();
    }

    public void sync(SyncEvent syncEvent) {
        slidingPanelViewManipulator.show();
        slidingPanelViewManipulator.setPlayingState(syncEvent.isPlaying);
        slidingPanelViewManipulator.update(syncEvent.position);
        if (itemQueryer == null || getId() != syncEvent.itemId) {
            setData(syncEvent.itemId);
        }
    }

    public void resetItem() {
        if (itemQueryer != null) {
            itemQueryer.stop();
            itemQueryer = null;
        }
    }

}
