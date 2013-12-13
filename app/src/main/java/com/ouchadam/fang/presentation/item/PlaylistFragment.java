package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.ItemSourceFetcher;
import com.ouchadam.fang.audio.PlaylistLoader;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FastModeHandler;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.panel.OverflowCallback;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class PlaylistFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem>, CursorBackedListFragment.OnLongClickListener<FullItem>, PlaylistActionMode.OnPlaylistActionMode, OnFastMode<FullItem> {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private final FastModeHandler fastModeHandler;
    private final ListWatcherRestorer listWatcherRestorer;

    private DetailsDisplayManager detailsDisplayManager;
    private PlaylistActionMode playlistActionMode;
    private SlidingPanelExposer panelController;
    private TextView spaceUsageText;
    private OverflowCallback overflowCallback;
    private PlaylistAdapter playlistAdapter;

    public PlaylistFragment() {
        this.fastModeHandler = new FastModeHandler();
        this.actionBarTitleSetter = new ActionBarTitleSetter();
        this.listWatcherRestorer = new ListWatcherRestorer();
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_playlist_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<FullItem> createAdapter() {
        playlistAdapter = new PlaylistAdapter(LayoutInflater.from(getActivity()), getActivity(), this, new ItemManipulator<PlaylistAdapter.ViewHolder>(childFetcher));
        return playlistAdapter;
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
                .withSelection(Tables.Playlist.ITEM_PLAYLIST + "!=?")
                .withSelectionArgs(new String[]{"0"})
                .withSorter(" CAST (" + Tables.Playlist.LIST_POSITION + " AS DECIMAL)" + " ASC")
                .build();
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        listWatcherRestorer.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
        panelController = Classes.from(activity);
        detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
        playlistActionMode = new PlaylistActionMode(activity, this);
        overflowCallback = Classes.from(activity);
        fastModeHandler.onAttach(activity);
    }

    @Override
    protected void onCreateViewExtra(View root) {
        super.onCreateViewExtra(root);
        spaceUsageText = Views.findById(root, R.id.space_usage);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.playlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_delete_all_downloads:
                onRemoveAll();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void onRemoveAll() {
        List<FullItem> items = getAdapter().getAll();
        DownloadDeleter.from(getActivity()).deleteAll(items);
        // TODO remove current playing item
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
        setOnItemLongClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        actionBarTitleSetter.set("Playlist");
        spaceUsageText.setText(new AvailableSpaceFetcher().formatted());
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
        if (!playlistActionMode.isInActionMode()) {
            detailsDisplayManager.displayItem(itemId);
        }
    }

    @Override
    public boolean onItemLongClick(TypedListAdapter<FullItem> adapterView, View view, int position, long itemId) {
        if (playlistActionMode.isInActionMode()) {
            return false;
        } else {
            playlistActionMode.onStart();
            allowChecking();
            setLongPressChecked(position);
            return true;
        }
    }

    @Override
    public void onDelete() {
        List<FullItem> selectedItems = getAllCheckedPositions();
        dismissCurrent(selectedItems);
        if (selectedItems != null && !selectedItems.isEmpty()) {
            DownloadDeleter.from(getActivity()).deleteItems(selectedItems);
            new PodcastPlayerEventBroadcaster(getActivity()).broadcast(new PlayerEvent.Factory().refresh());
        } else {
            Toast.makeText(getActivity(), "Failed to delete, couldn't find and podcasts", Toast.LENGTH_SHORT).show();
        }
    }

    private void dismissCurrent(List<FullItem> selectedItems) {
        for (FullItem selectedItem : selectedItems) {
            if (panelController.getId() == selectedItem.getItemId()) {
                overflowCallback.onDismissDrawer();
                return;
            }
        }
    }

    @Override
    public void onActionModeFinish() {
        deselectAll();
        disallowChecking();
    }

    @Override
    protected boolean canRefresh() {
        return false;
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
