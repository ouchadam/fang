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
        ContentProviderOperationValues playlistBuilder = newInsertFor(Uris.PLAYLIST);
        playlistBuilder.withValue(Tables.Playlist.ITEM_PLAYLIST.name(), item.getItemId());
        playlistBuilder.withValue(Tables.Playlist.LIST_POSITION.name(), item.getListPosition());
        operations.add(playlistBuilder);

        ContentProviderOperationValues itemPlayBuilder = newInsertFor(Uris.ITEM_PLAY);
        itemPlayBuilder.withValue(Tables.ItemPlay.ITEM_ID.name(), item.getItemId());
        itemPlayBuilder.withValue(Tables.ItemPlay.DOWNLOAD_ID.name(), item.getDownloadId());
        operations.add(itemPlayBuilder);
    }

}