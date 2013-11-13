package com.ouchadam.fang.persistance.database.marshaller;

import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public abstract class BaseMarshaller<T> implements Marshaller<T, List<ContentProviderOperationValues>> {

    private final OperationWrapper operationWrapper;

    public BaseMarshaller(OperationWrapper operationWrapper) {
        this.operationWrapper = operationWrapper;
    }

    protected ContentProviderOperationValues newInsertFor(Uris uris) {
        return operationWrapper.newInsert(uris);
    }

    protected ContentProviderOperationValues newUpdateFor(Uris uris) {
        return operationWrapper.newUpdate(uris);
    }
}
