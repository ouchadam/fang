package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class PlaylistMarshaller extends BaseMarshaller<ItemToPlaylist>  {

    private List<ContentProviderOperationValues> operations;

    public PlaylistMarshaller(OperationWrapper operationWrapper) {
        super(operationWrapper);
    }

    @Override
    public List<ContentProviderOperationValues> marshall(ItemToPlaylist what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(ItemToPlaylist item) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.PLAYLIST);
        itemBuilder.withValue(Tables.Playlist.ITEM_ID.name(), item.getItemId());
        itemBuilder.withValue(Tables.Playlist.DOWNLOAD_ID.name(), item.getDownloadId());
        itemBuilder.withValue(Tables.Playlist.LIST_POSITION.name(), item.getListPosition());
        operations.add(itemBuilder);
    }

}