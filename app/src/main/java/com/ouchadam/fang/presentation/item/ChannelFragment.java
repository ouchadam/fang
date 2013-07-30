package com.ouchadam.fang.presentation.item;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class ChannelFragment extends CursorBackedListFragment<Channel> {

    @Override
    protected AbsListView getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return (AbsListView) inflater.inflate(R.layout.fragment_channel_list, container, false);
    }

    @Override
    protected TypedListAdapter<Channel> getAdapter() {
        return new ChannelAdapter(getActivity(), LayoutInflater.from(getActivity()));
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.FULL_CHANNEL)).build();
    }

    @Override
    protected CursorMarshaller<Channel> getMarshaller() {
        return getChannelMarshaller();
    }

    private CursorMarshaller<Channel> getChannelMarshaller() {
        return new ChannelSummaryMarshaller();
    }

    private static class ChannelSummaryMarshaller implements CursorMarshaller<Channel> {
        @Override
        public Channel marshall(Cursor cursor) {
            String title = cursor.getString(cursor.getColumnIndexOrThrow(Tables.Channel.TITLE.name()));
            String imageUrl = cursor.getString(cursor.getColumnIndexOrThrow(Tables.ChannelImage.URL.name()));
            return new Channel(title, "", new Image(imageUrl, "", "", 0, 0), null, null);
        }
    }
}
