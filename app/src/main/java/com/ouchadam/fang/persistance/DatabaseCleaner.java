package com.ouchadam.fang.persistance;

import android.content.ContentProviderOperation;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

import java.util.List;

import static android.content.ContentProviderOperation.newDelete;

public class DatabaseCleaner {

    private final ContentProviderOperationExecutable executor;

    public DatabaseCleaner(ContentProviderOperationExecutable executor) {
        this.executor = executor;
    }

    public boolean deleteTestData() {
        return deleteEntireTable(Uris.CHANNEL, Uris.ITEM, Uris.IMAGE, Uris.PLAYLIST);
    }

    public boolean deletePlaylist() {
        return deleteEntireTable(Uris.PLAYLIST);
    }

    private boolean deleteEntireTable(Uris... uris) {
        List<ContentProviderOperation> operations = Collections.newArrayList();
        for (Uris uri : uris) {
            ContentProviderOperation operation = newDelete(FangProvider.getUri(uri)).build();
            operations.add(operation);
        }
        return execute(operations);
    }

    private boolean execute(List<ContentProviderOperation> contentProviderOperations) {
        try {
            executor.execute(contentProviderOperations);
            return true;
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
        return false;
    }

    public void deleteIdsFromPlaylist(long[] itemIds) {
        List<ContentProviderOperation> operations = Collections.newArrayList();
        for (long itemId : itemIds) {
            ContentProviderOperation.Builder builder = newDelete(FangProvider.getUri(Uris.PLAYLIST));
            builder.withSelection(Tables.Playlist.ITEM_ID + "=?", new String[]{Long.toString(itemId)});
            operations.add(builder.build());
        }
        execute(operations);
    }
}