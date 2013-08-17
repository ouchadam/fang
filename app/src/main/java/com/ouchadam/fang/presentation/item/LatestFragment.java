package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.novoda.notils.android.ClassCaster;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.ChannelItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Uris;

import com.ouchadam.fang.presentation.controller.ChannelItemMarshaller;
import com.ouchadam.fang.presentation.controller.ItemMarshaller;
import novoda.android.typewriter.cursor.CursorMarshaller;

public class LatestFragment extends CursorBackedListFragment<ChannelItem> implements OnItemClickListener<ChannelItem> {

    private Downloader downloader;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        downloader = ClassCaster.toListener(activity);
    }

    @Override
    protected AbsListView getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return (AbsListView) inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected TypedListAdapter<ChannelItem> createAdapter() {
        return new ItemAdapter(LayoutInflater.from(getActivity()), getActivity());
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder().withUri(FangProvider.getUri(Uris.ITEM_WITH_IMAGE)).build();
    }

    @Override
    protected CursorMarshaller<ChannelItem> getMarshaller() {
        return new ChannelItemMarshaller();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TypedListAdapter<ChannelItem> adapter, int position, long itemId) {
        ChannelItem item = adapter.getItem(position);
        downloadItem(item.getItem(), itemId);
    }

    private void downloadItem(Item item, long itemId) {
        ItemDownload downloadable = ItemDownload.from(item);
        DownloadId downloadId = downloader.keep(downloadable);
        downloader.store(downloadId, itemId);

        new AddToPlaylistPersister(getActivity().getContentResolver()).persist(ItemToPlaylist.from(item, downloadId.value()));
        downloader.watch(downloadId, new NotificationWatcher(getActivity(), downloadable, downloadId));
    }

}
