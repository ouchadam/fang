package com.ouchadam.fang.presentation.item;

import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import android.widget.AdapterView;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.PlaylistItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class LatestFragment extends CursorBackedListFragment<Item> implements OnItemClickListener<Item> {

    @Override
    protected AbsListView getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return (AbsListView) inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<Item> getAdapter() {
        return new ItemAdapter(LayoutInflater.from(getActivity()));
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.ITEM)).build();
    }

    @Override
    protected CursorMarshaller<Item> getMarshaller() {
        return getItemMarshaller();
    }

    private CursorMarshaller<Item> getItemMarshaller() {
        return new CursorMarshaller<Item>() {
            @Override
            public Item marshall(Cursor cursor) {
                String title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Item.TITLE.name()));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(Tables.Item._id.name()));
                return new Item(title, "", "", null, "", "", id);
            }
        };
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TypedListAdapter<Item> adapter, int position) {
        Item item = adapter.getItem(position);
        new AddToPlaylistPersister(getActivity().getContentResolver()).persist(PlaylistItem.from(item));
    }
}
