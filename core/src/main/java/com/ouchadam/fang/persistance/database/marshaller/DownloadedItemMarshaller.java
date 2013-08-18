package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class DownloadedItemMarshaller extends BaseMarshaller<Long>  {

    private List<ContentProviderOperationValues> operations;

    public DownloadedItemMarshaller(OperationWrapper operationWrapper) {
        super(operationWrapper);
    }

    @Override
    public List<ContentProviderOperationValues> marshall(Long what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(long downloadId) {
        ContentProviderOperationValues itemBuilder = newUpdateFor(Uris.PLAYLIST);
        itemBuilder.withSelection(Tables.Playlist.DOWNLOAD_ID + "=?", new String[] { String.valueOf(downloadId)});
        itemBuilder.withValue(Tables.Playlist.DOWNLOADED.name(), 1);
        operations.add(itemBuilder);
    }

}