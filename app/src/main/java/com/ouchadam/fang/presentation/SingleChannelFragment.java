package com.ouchadam.fang.presentation;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.item.ChildFetcher;
import com.ouchadam.fang.presentation.item.CursorBackedListFragment;
import com.ouchadam.fang.presentation.item.DetailsDisplayManager;
import com.ouchadam.fang.presentation.item.ItemAdapter;
import com.ouchadam.fang.presentation.item.ItemManipulator;
import com.ouchadam.fang.presentation.item.LazyListItemWatcher;
import com.ouchadam.fang.presentation.item.NavigatorForResult;
import com.ouchadam.fang.presentation.item.OnFastMode;
import com.ouchadam.fang.presentation.item.OnItemClickListener;
import com.ouchadam.fang.presentation.item.PlaylistAdapter;
import com.ouchadam.fang.presentation.item.TypedListAdapter;
import com.ouchadam.fang.presentation.panel.SlidingPanelController;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class SingleChannelFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem>, OnFastMode<FullItem> {

    private static final String CHANNEL = "channel";
    private final FastModeHandler fastModeHandler;

    private DetailsDisplayManager detailsDisplayManager;

    public static SingleChannelFragment newInstance(String channelTitle) {
        SingleChannelFragment singleChannelFragment = new SingleChannelFragment();
        Bundle arguments = new Bundle();
        arguments.putString(CHANNEL, channelTitle);
        singleChannelFragment.setArguments(arguments);
        return singleChannelFragment;
    }


    public SingleChannelFragment() {
        this.fastModeHandler = new FastModeHandler();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        SlidingPanelExposer panelController = Classes.from(activity);
        this.detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
        fastModeHandler.onAttach(activity);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    protected TypedListAdapter<FullItem> createAdapter() {
        return new ItemAdapter(LayoutInflater.from(getActivity()), getActivity(), this, new ItemManipulator<ItemAdapter.ViewHolder>(childFetcher));
    }

    private final ChildFetcher childFetcher = new ChildFetcher() {
        @Override
        public View getChildAt(int itemIdPosition) {
            return getList().getChildAt(itemIdPosition - getList().getFirstVisiblePosition());
        }
    };


    @Override
    protected Query getQueryValues() {
        return new Query.Builder()
                .withUri(FangProvider.getUri(Uris.FULL_ITEM))
                .withSelection(Tables.Item.ITEM_CHANNEL + "=?")
                .withSelectionArgs(new String[] { getChannel() })
                .withSorter(" CAST (" + Tables.Item.PUBDATE + " AS DECIMAL)" + " DESC")
                .build();
    }

    private String getChannel() {
        return getArguments().getString(CHANNEL);
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    @Override
    public void onItemClick(TypedListAdapter<FullItem> adapter, int position, long itemId) {
        detailsDisplayManager.displayItem(itemId);
    }

    @Override
    public void onFastMode(FullItem what) {
        fastModeHandler.onFastMode(what, new LazyListItemWatcher((ListItemWatcher.ItemWatcher) getAdapter()));
    }

    @Override
    public boolean isPlaying(long itemId) {
        return fastModeHandler.isPlaying(itemId);
    }

    @Override
    public boolean isEnabled() {
        return fastModeHandler.isEnabled();
    }

}


