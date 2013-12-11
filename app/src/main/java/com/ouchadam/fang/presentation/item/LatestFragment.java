package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.watcher.LazyWatcher;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FastModeHandler;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class LatestFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem>, OnFastMode<FullItem> {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private final ListWatcherRestorer listWatcherRestorer;
    private DetailsDisplayManager detailsDisplayManager;
    private LastUpdateController lastUpdateController;
    private FastModeHandler fastModeHandler;

    public LatestFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
        this.fastModeHandler = new FastModeHandler();
        this.listWatcherRestorer = new ListWatcherRestorer();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
        listWatcherRestorer.onAttach(activity);
        SlidingPanelExposer panelController = Classes.from(activity);
        detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
        fastModeHandler.onAttach(activity);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected void onCreateViewExtra(View root) {
        super.onCreateViewExtra(root);
        lastUpdateController = LastUpdateController.from(root);
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
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundles.restoreInstanceState(this, savedInstanceState);
        actionBarTitleSetter.set("Latest");
        lastUpdateController.handleLastUpdated();
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder()
                .withUri(FangProvider.getUri(Uris.FULL_ITEM))
                .withSorter(" CAST (" + Tables.Item.PUBDATE + " AS DECIMAL)" + " DESC")
                .build();
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onDataUpdated(List<FullItem> data) {
        super.onDataUpdated(data);
        if (!data.isEmpty()) {
            listWatcherRestorer.restoreWatcher((ListItemWatcher.ItemWatcher) getAdapter());
        }
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
