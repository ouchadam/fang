package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

public class BaseMarshaller {

    private final OperationWrapper operationWrapper;

    public BaseMarshaller(OperationWrapper operationWrapper) {
        this.operationWrapper = operationWrapper;
    }

    protected ContentProviderOperationValues newInsertFor(Uris uris) {
        return operationWrapper.newInsert(uris);
    }
}
