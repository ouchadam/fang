package com.ouchadam.fang.presentation.panel;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.support.v4.app.LoaderManager;

import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.presentation.PlayerEvent;

public class SlidingPanelController implements SlidingPanelExposer, SlidingPanelViewManipulator.OnDownloadClickListener {

    private final Downloader downloader;
    private final Context context;
    private final LoaderManager loaderManager;
    private final SlidingPanelViewManipulator slidingPanelViewManipulator;
    private Broadcaster<PlayerEvent> playerBroadcaster;

    private ItemQueryer itemQueryer;

    public SlidingPanelController(Downloader downloader, Context context, LoaderManager loaderManager, SlidingPanelViewManipulator slidingPanelViewManipulator, Broadcaster<PlayerEvent> playerBroadcaster) {
        this.downloader = downloader;
        this.context = context;
        this.loaderManager = loaderManager;
        this.slidingPanelViewManipulator = slidingPanelViewManipulator;
        this.playerBroadcaster = playerBroadcaster;
        slidingPanelViewManipulator.setOnDownloadClickedListener(this);
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
                Uri source = getSourceUri(item);
                playerBroadcaster.broadcast(new PlayerEvent.Factory().newSource(item.getItemId(), source));
                playerBroadcaster.broadcast(new PlayerEvent.Factory().goTo(item.getInitialPlayPosition()));
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
                play();
            } else {
                pause();
            }
        }
    };

    @Override
    public void showExpanded() {
        // TODO disable nav drawer
        slidingPanelViewManipulator.expand();
    }

    @Override
    public void showPanel() {
        slidingPanelViewManipulator.show();
    }

    private void play() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().play(slidingPanelViewManipulator.getPosition()));
    }

    private Uri getSourceUri(FullItem item) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager.getUriForDownloadedFile(item.getDownloadId());
    }

    private void pause() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().pause());
    }

    public void close() {
        slidingPanelViewManipulator.close();
    }

    public boolean isShowing() {
        return slidingPanelViewManipulator.isShowing();
    }

    @Override
    public void onDownloadClicked(FullItem fullItem) {
        downloadItem(fullItem.getItem(), fullItem.getItemId());
    }

    private void downloadItem(Item item, long itemId) {
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        downloader.store(downloadId, itemId);

        new AddToPlaylistPersister(context.getContentResolver()).persist(ItemToPlaylist.from(item, downloadId.value()));
        downloader.watch(downloadId, new NotificationWatcher(context, downloadable, downloadId));
    }

    public void sync(SyncEvent syncEvent) {
        slidingPanelViewManipulator.setPlayingState(syncEvent.isPlaying);
        slidingPanelViewManipulator.update(syncEvent.position);
        if (syncEvent.isFresh()) {
            // TODO: do nothing for now...
        } else if (itemQueryer == null) {
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
