package com.ouchadam.fang.presentation.controller;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import android.view.View;
import android.widget.Toast;
import com.ouchadam.fang.domain.item.Item;

public class SlidingPanelController implements SlidingPanelExposer {

    private final Context context;
    private final LoaderManager loaderManager;
    private final SlidingPanelViewManipulator slidingPanelViewManipulator;

    private ItemQueryer itemQueryer;

    public SlidingPanelController(Context context, LoaderManager loaderManager, SlidingPanelViewManipulator slidingPanelViewManipulator) {
        this.context = context;
        this.loaderManager = loaderManager;
        this.slidingPanelViewManipulator = slidingPanelViewManipulator;
    }

    @Override
    public void setData(int itemColumnId) {
        if (itemQueryer != null) {
            itemQueryer.stop();
        }
        itemQueryer = new ItemQueryer(context, itemColumnId, loaderManager, onItem);
        itemQueryer.query();
    }

    private final ItemQueryer.OnItemListener onItem = new ItemQueryer.OnItemListener() {
        @Override
        public void onItem(Item item) {
            initialiseViews(item);
        }
    };

    private void initialiseViews(Item item) {
        slidingPanelViewManipulator.setBarText(item.getTitle());
        slidingPanelViewManipulator.setTopBarPlayClicked(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "Play", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
