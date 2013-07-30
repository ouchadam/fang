package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.PlaylistItem;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class PlaylistMarshaller extends BaseMarshaller<PlaylistItem>  {

    private List<ContentProviderOperationValues> operations;

    public PlaylistMarshaller(OperationWrapper operationWrapper) {
        super(operationWrapper);
    }

    @Override
    public List<ContentProviderOperationValues> marshall(PlaylistItem what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(PlaylistItem item) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.PLAYLIST);
        itemBuilder.withValue(Tables.Playlist.ITEM_ID.name(), item.getItemId());
        operations.add(itemBuilder);
    }

}