package com.ouchadam.fang.presentation.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.view.View;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;

import java.io.IOException;

public class SlidingPanelController implements SlidingPanelExposer, SlidingPanelViewManipulator.OnPanelChangeListener, SlidingPanelViewManipulator.OnDownloadClickListener {

    private final Downloader downloader;
    private final Context context;
    private final LoaderManager loaderManager;
    private final SlidingPanelViewManipulator slidingPanelViewManipulator;
    private final PodcastPlayer podcastPlayer;

    private ItemQueryer itemQueryer;

    public SlidingPanelController(Downloader downloader, Context context, LoaderManager loaderManager, SlidingPanelViewManipulator slidingPanelViewManipulator) {
        this.downloader = downloader;
        this.context = context;
        this.loaderManager = loaderManager;
        this.slidingPanelViewManipulator = slidingPanelViewManipulator;
        podcastPlayer = new PodcastPlayer(new MediaPlayer(), slidingPanelViewManipulator);
        slidingPanelViewManipulator.setOnPanelExpandListener(this);
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

    @Override
    public void show() {
        slidingPanelViewManipulator.expand();
    }

    private final ItemQueryer.OnItemListener onItem = new ItemQueryer.OnItemListener() {
        @Override
        public void onItem(FullItem item) {
            initialiseViews(item);
        }
    };

    private void initialiseViews(final FullItem item) {
        slidingPanelViewManipulator.fromItem(item);
        slidingPanelViewManipulator.setMediaClickedListener(new SlidingPanelViewManipulator.OnMediaClickListener() {
            @Override
            public void onMediaClicked(SlidingPanelViewManipulator.MediaPressed mediaPressed) {
                if (mediaPressed == SlidingPanelViewManipulator.MediaPressed.PLAY) {
                    playItem(item);
                } else {
                    pause();
                }
            }
        });
    }

    private void pause() {
        podcastPlayer.pause();
    }

    private void playItem(FullItem fullItem) {
        if (!podcastPlayer.isPaused()) {
            setSource(fullItem);
        }
        podcastPlayer.play();
    }

    private void setSource(FullItem fullItem) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri itemUri = downloadManager.getUriForDownloadedFile(fullItem.getDownloadId());

        try {
            podcastPlayer.setSource(context, itemUri);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("couldn't find : " + itemUri);
        }
    }

    public void close() {
        slidingPanelViewManipulator.close();
    }

    public boolean isShowing() {
        return slidingPanelViewManipulator.isShowing();
    }

    @Override
    public void onPanelExpanded(View panel) {
        // TODO switch views, maybe this shouldnt be here
    }

    @Override
    public void onPanelCollapsed(View panel) {
        // TODO switch views, maybe this shouldnt be here
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

}
