package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

import java.util.List;

public abstract class BaseMarshaller<T> implements Marshaller<T, List<ContentProviderOperationValues>> {

    private final OperationWrapper operationWrapper;

    public BaseMarshaller(OperationWrapper operationWrapper) {
        this.operationWrapper = operationWrapper;
    }

    protected ContentProviderOperationValues newInsertFor(Uris uris) {
        return operationWrapper.newInsert(uris);
    }
}
