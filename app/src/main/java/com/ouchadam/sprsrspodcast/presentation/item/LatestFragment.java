package com.ouchadam.sprsrspodcast.presentation.item;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.FangProvider;
import com.ouchadam.sprsrspodcast.persistance.Query;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class LatestFragment extends CursorBackedListFragment<Item> {

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
                return new Item(title, "", "", null, "", "");
            }
        };
    }

}
