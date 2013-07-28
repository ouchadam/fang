package com.ouchadam.sprsrspodcast.persistance;

import android.content.ContentProviderOperation;

import com.novoda.notils.java.Collections;
import com.ouchadam.sprsrspodcast.persistance.database.Executable;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;

import java.util.List;

import static android.content.ContentProviderOperation.newDelete;

public class DatabaseCleaner {

    private final ContentProviderOperationExecutable executor;

    public DatabaseCleaner(ContentProviderOperationExecutable executor) {
        this.executor = executor;
    }

    public void deleteAllTables() {
        List<ContentProviderOperation> operations = Collections.newArrayList();
        for (Uris uri : Uris.values()) {
            operations.add(newDelete(FangProvider.getUri(uri)).build());
        }
        execute(operations);
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
}