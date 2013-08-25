package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class PositionMarshaller extends BaseMarshaller<PodcastPosition>  {

    private final long itemId;

    private List<ContentProviderOperationValues> operations;

    public PositionMarshaller(long itemId, OperationWrapper operationWrapper) {
        super(operationWrapper);
        this.itemId = itemId;
    }

    @Override
    public List<ContentProviderOperationValues> marshall(PodcastPosition what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(PodcastPosition position) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.PLAYLIST);
        itemBuilder.withValue(Tables.Playlist.ITEM_ID.name(), itemId);
        itemBuilder.withValue(Tables.Playlist.PLAY_POSITION.name(), position.value());
        itemBuilder.withValue(Tables.Playlist.MAX_DURATION.name(), position.getDuration());
        operations.add(itemBuilder);
    }

}