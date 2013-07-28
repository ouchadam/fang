package com.ouchadam.sprsrspodcast.persistance;

import android.content.ContentProviderOperation;

import com.ouchadam.sprsrspodcast.persistance.database.Executable;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.Marshaller;

import java.util.ArrayList;
import java.util.List;

public class Persister<T> {

    private final ContentProviderOperationExecutable executor;
    private final Marshaller<T, List<ContentProviderOperationValues>> marshaller;

    public Persister(ContentProviderOperationExecutable executor, Marshaller<T, List<ContentProviderOperationValues>> marshaller) {
        this.executor = executor;
        this.marshaller = marshaller;
    }

    public void persist(T what) throws Executable.ExecutionFailure {
        List<ContentProviderOperationValues> coreOperations = marshaller.marshall(what);

        List<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>();
        for (ContentProviderOperationValues operation : coreOperations) {
            operations.add(((OperationWrapperImpl.BuilderWrapper) operation).build());
        }
        executor.execute(operations);
    }

}