package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
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

public class LatestFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem>, PullToRefreshAttacher.OnRefreshListener {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private DetailsDisplayManager detailsDisplayManager;
    private LastUpdateController lastUpdateController;
    private PullToRefreshExposer pullToRefreshExposer;
    private ChannelRefreshCompleteReceiver channelRefreshCompleteReceiver;

    public LatestFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
        SlidingPanelExposer panelController = Classes.from(activity);
        detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
        pullToRefreshExposer = Classes.from(activity);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected void onCreateViewExtra(View root) {
        super.onCreateViewExtra(root);
        lastUpdateController = LastUpdateController.from(root);
        pullToRefreshExposer.getPullToRefresh().addRefreshableView(Views.findById(root, R.id.list), this);
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
        initPullToRefresh();
    }

    private void initPullToRefresh() {
        pullToRefreshExposer.getPullToRefresh().setRefreshing(downloadServiceIsRunning());
        channelRefreshCompleteReceiver = new ChannelRefreshCompleteReceiver(pullToRefreshExposer.getPullToRefresh());
        getActivity().registerReceiver(channelRefreshCompleteReceiver, new IntentFilter(ChannelFeedDownloadService.ACTION_CHANNEL_FEED_COMPLETE));
    }

    private boolean downloadServiceIsRunning() {
        return AndroidUtils.isServiceRunning(ChannelFeedDownloadService.class, getActivity());
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

    @Override
    public void onRefreshStarted(View view) {
        pullToRefreshExposer.getPullToRefresh().setRefreshing(true);
        Intent refreshIntent = FeedServiceInfo.refresh(getActivity());
        getActivity().startService(refreshIntent);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(channelRefreshCompleteReceiver, new IntentFilter(ChannelFeedDownloadService.ACTION_CHANNEL_FEED_COMPLETE));
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(channelRefreshCompleteReceiver);
    }

    private static class ChannelRefreshCompleteReceiver extends BroadcastReceiver {

        private final PullToRefreshAttacher pullToRefreshAttacher;

        private ChannelRefreshCompleteReceiver(PullToRefreshAttacher pullToRefreshAttacher) {
            this.pullToRefreshAttacher = pullToRefreshAttacher;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent != null && ChannelFeedDownloadService.ACTION_CHANNEL_FEED_COMPLETE.equals(intent.getAction())) {
                pullToRefreshAttacher.setRefreshComplete();
            }
        }
    }

}
