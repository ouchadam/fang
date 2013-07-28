package com.ouchadam.sprsrspodcast.persistance;

import android.content.ContentProviderOperation;

import com.ouchadam.sprsrspodcast.persistance.database.Executable;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.Marshaller;

import java.util.ArrayList;
import java.util.List;

public class Persister<T> {

    private final ContentProviderOperationExecutable executor;
    private final Marshaller<T, List<ContentProviderOperationValues>> marshaller;

    public Persister(ContentProviderOperationExecutable executor, BaseMarshaller<T> marshaller) {
        this.executor = executor;
        this.marshaller = marshaller;
    }

    public void persist(T what) throws Executable.ExecutionFailure {
        executor.execute(getContentProviderOperations(what));
    }

    private List<ContentProviderOperation> getContentProviderOperations(T what) {
        List<ContentProviderOperationValues> coreOperations = marshaller.marshall(what);
        List<ContentProviderOperation> operations = new ArrayList<ContentProviderOperation>(coreOperations.size());
        for (ContentProviderOperationValues operation : coreOperations) {
            operations.add(((OperationWrapperImpl.BuilderWrapper) operation).build());
        }
        return operations;
    }

}