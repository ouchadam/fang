package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.novoda.notils.meta.AndroidUtils;
import com.ouchadam.fang.R;
import com.ouchadam.fang.debug.ChannelFeedDownloadService;
import com.ouchadam.fang.debug.FeedServiceInfo;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.controller.PullToRefreshExposer;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

import novoda.android.typewriter.cursor.CursorMarshaller;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public class LatestFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem> {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private DetailsDisplayManager detailsDisplayManager;
    private LastUpdateController lastUpdateController;

    public LatestFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
        SlidingPanelExposer panelController = Classes.from(activity);
        detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
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
        return new ItemAdapter(LayoutInflater.from(getActivity()), getActivity());
    }

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
    public void onItemClick(TypedListAdapter<FullItem> adapter, int position, long itemId) {
        detailsDisplayManager.displayItem(itemId);
    }

}
