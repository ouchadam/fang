package com.ouchadam.fang.presentation.panel;

import android.content.Context;
import android.support.v4.app.LoaderManager;

import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.audio.SyncEvent;
import com.ouchadam.fang.audio.event.PlayerEventInteractionManager;
import com.ouchadam.fang.domain.FullItem;

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
            switch (mediaPressed) {

                case PLAY:
                    playerEventInteractionManager.play();
                    break;

                case PAUSE:
                    playerEventInteractionManager.pause();
                    break;

                case REWIND:
                    playerEventInteractionManager.rewind();
                    break;

                case FAST_FORWARD:
                    playerEventInteractionManager.fastForward();
                    break;

                case NEXT:
                    playerEventInteractionManager.next();
                    break;

                case PREVIOUS:
                    playerEventInteractionManager.previous();
                    break;

                default:
                    throw new IllegalAccessError("Unhandled media pressed state" + mediaPressed.name());
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
    public boolean isPlaying(long itemId) {
        return getId() == itemId ? slidingPanelViewManipulator.isPlaying() : false;
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
