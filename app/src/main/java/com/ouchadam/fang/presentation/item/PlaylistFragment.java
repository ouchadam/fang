package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.app.DownloadManager;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;
import com.novoda.notils.android.ClassCaster;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.controller.ItemMarshaller;
import com.ouchadam.fang.presentation.controller.SlidingPanelExposer;
import novoda.android.typewriter.cursor.CursorMarshaller;

import java.util.List;

public class PlaylistFragment extends CursorBackedListFragment<Item> implements OnItemClickListener<Item> {

    private Downloader downloader;
    private SlidingPanelExposer panelController;
    private boolean hasRestored;

    public PlaylistFragment() {
        this.hasRestored = false;
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<Item> createAdapter() {
        return new ExampleListAdapter(LayoutInflater.from(getActivity()));
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.PLAYLIST_ITEM)).build();
    }

    @Override
    protected CursorMarshaller<Item> getMarshaller() {
        return getItemMarshaller();
    }

    private CursorMarshaller<Item> getItemMarshaller() {
        return new ItemMarshaller();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        downloader = ClassCaster.toListener(activity);
        panelController = ClassCaster.toListener(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onDataUpdated(List<Item> data) {
        super.onDataUpdated(data);
        if (!data.isEmpty() && !hasRestored) {
            downloader.restore(new LazyListItemWatcher((ListItemWatcher.ItemWatcher) getAdapter()));
            hasRestored = true;
        }
    }

    @Override
    public void onItemClick(TypedListAdapter<Item> adapter, int position, long itemId) {
        // TODO play by default?
        Item item = adapter.getItem(position);
        panelController.setData(item.getId());
    }

}
