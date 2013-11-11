package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;
import novoda.android.typewriter.cursor.CursorMarshaller;

import java.util.List;

public class PlaylistFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem> {

    private final ActionBarTitleSetter actionBarTitleSetter;

    private Downloader downloader;
    private SlidingPanelExposer panelController;
    private boolean hasRestored;

    public PlaylistFragment() {
        this.hasRestored = false;
        this.actionBarTitleSetter = new ActionBarTitleSetter();
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<FullItem> createAdapter() {
        return new PlaylistAdapter(LayoutInflater.from(getActivity()), getActivity());
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder()
                .withUri(FangProvider.getUri(Uris.FULL_ITEM))
                .withSelection(Tables.Playlist.DOWNLOAD_ID + "!=?")
                .withSelectionArgs(new String[]{"0"})
                .build();
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        downloader = Classes.from(activity);
        panelController = Classes.from(activity);
        actionBarTitleSetter.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.playlist, menu);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        actionBarTitleSetter.set("Playlist");
    }

    @Override
    public void onDataUpdated(List<FullItem> data) {
        super.onDataUpdated(data);
        if (!data.isEmpty() && !hasRestored) {
            downloader.restore(new LazyListItemWatcher((ListItemWatcher.ItemWatcher) getAdapter()));
            hasRestored = true;
        }
    }

    @Override
    public void onItemClick(TypedListAdapter<FullItem> adapter, int position, long itemId) {
        panelController.showPanel();
        panelController.setData(itemId);
    }

}
