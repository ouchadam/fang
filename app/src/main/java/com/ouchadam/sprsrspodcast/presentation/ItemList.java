package com.ouchadam.sprsrspodcast.presentation;

import android.app.Activity;
import android.app.Fragment;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.DataUpdater;
import com.ouchadam.sprsrspodcast.persistance.FangProvider;
import com.ouchadam.sprsrspodcast.persistance.Query;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class ItemList extends Fragment implements DataUpdater.DataUpdatedListener<Item> {

    private DataUpdater<Item> dataUpdater;
    private ItemAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);
        ((ListView) root).setAdapter(getAdapter());
        return root;
    }

    private ListAdapter getAdapter() {
        ItemAdapter itemAdapter = new ItemAdapter(LayoutInflater.from(getActivity()));
        this.adapter = itemAdapter;
        return itemAdapter;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        queryForData();
    }

    private void queryForData() {
        if (dataUpdater != null) {
            dataUpdater.stopWatchingData();
        }
        dataUpdater = new DataUpdater<Item>(getActivity(), getQueryValues(), getItemMarshaller(), this, getLoaderManager());
        dataUpdater.startWatchingData();
    }

    public Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.ITEM)).build();
    }

    public CursorMarshaller<Item> getItemMarshaller() {
        return new CursorMarshaller<Item>() {
            @Override
            public Item marshall(Cursor cursor) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.TITLE.name()));
                return new Item(title, "", "", null, "", "");
            }
        };
    }

    @Override
    public void onDataUpdated(List<Item> data) {
        updateAdapter(data);
    }

    private void updateAdapter(List<Item> data) {
        if (data != null && !data.isEmpty()) {
            adapter.updateData(data);
        }
    }

}
