package com.ouchadam.fang.presentation.controller;

import android.app.DownloadManager;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.view.View;
import com.ouchadam.fang.domain.LocalItem;

import java.io.IOException;

public class SlidingPanelController implements SlidingPanelExposer, SlidingPanelViewManipulator.OnPanelChangeListener {

    private final Context context;
    private final LoaderManager loaderManager;
    private final SlidingPanelViewManipulator slidingPanelViewManipulator;
    private final PodcastPlayer podcastPlayer;

    private ItemQueryer itemQueryer;

    public SlidingPanelController(Context context, LoaderManager loaderManager, SlidingPanelViewManipulator slidingPanelViewManipulator) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.slidingPanelViewManipulator = slidingPanelViewManipulator;
        podcastPlayer = new PodcastPlayer(new MediaPlayer(), slidingPanelViewManipulator);
        slidingPanelViewManipulator.setOnPanelExpandListener(this);
    }

    @Override
    public void setData(int itemColumnId) {
        if (itemQueryer != null) {
            itemQueryer.stop();
        }
        itemQueryer = new ItemQueryer(context, itemColumnId, loaderManager, onItem);
        itemQueryer.query();
    }

    @Override
    public void show() {
        slidingPanelViewManipulator.expand();
    }

    private final ItemQueryer.OnItemListener onItem = new ItemQueryer.OnItemListener() {
        @Override
        public void onItem(LocalItem item) {
            initialiseViews(item);
        }
    };

    private void initialiseViews(final LocalItem item) {
        slidingPanelViewManipulator.fromItem(item.getItem());
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

    private void playItem(LocalItem localItem) {
        if (!podcastPlayer.isPaused()) {
            setSource(localItem);
        }
        podcastPlayer.play();
    }

    private void setSource(LocalItem localItem) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        Uri itemUri = downloadManager.getUriForDownloadedFile(localItem.getDownloadId());

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
}
