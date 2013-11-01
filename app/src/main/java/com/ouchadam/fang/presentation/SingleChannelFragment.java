package com.ouchadam.fang.presentation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.item.CursorBackedListFragment;
import com.ouchadam.fang.presentation.item.ItemAdapter;
import com.ouchadam.fang.presentation.item.TypedListAdapter;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class SingleChannelFragment extends CursorBackedListFragment<FullItem> {

    private static final String CHANNEL = "channel";

    public static SingleChannelFragment newInstance(String channelTitle) {
        SingleChannelFragment singleChannelFragment = new SingleChannelFragment();
        Bundle arguments = new Bundle();
        arguments.putString(CHANNEL, channelTitle);
        singleChannelFragment.setArguments(arguments);
        return singleChannelFragment;
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<FullItem> createAdapter() {
        return new ItemAdapter(LayoutInflater.from(getActivity()), getActivity());
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder()
                .withUri(FangProvider.getUri(Uris.FULL_ITEM))
                .withSelection(Tables.Item.CHANNEL + "=?")
                .withSelectionArgs(new String[] { getChannel() })
                .build();
    }

    private String getChannel() {
        return getArguments().getString(CHANNEL);
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

}


