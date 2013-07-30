package com.ouchadam.fang.persistance;

import android.content.ContentProviderOperation;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.Uris;

import java.util.List;

import static android.content.ContentProviderOperation.newDelete;

public class DatabaseCleaner {

    private final ContentProviderOperationExecutable executor;

    public DatabaseCleaner(ContentProviderOperationExecutable executor) {
        this.executor = executor;
    }

    public boolean deleteTestData() {
        return delete(Uris.CHANNEL, Uris.ITEM, Uris.IMAGE);
    }

    private boolean delete(Uris... uris) {
        List<ContentProviderOperation> operations = Collections.newArrayList();
        for (Uris uri : uris) {
            ContentProviderOperation operation = newDelete(FangProvider.getUri(uri)).build();
            operations.add(operation);
        }
        return execute(operations);
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