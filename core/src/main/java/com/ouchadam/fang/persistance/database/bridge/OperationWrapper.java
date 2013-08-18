package com.ouchadam.fang.persistance.database.bridge;

import com.ouchadam.fang.persistance.database.Uris;

public interface OperationWrapper {
    ContentProviderOperationValues newInsert(Uris uri);
    ContentProviderOperationValues newUpdate(Uris uri);
}
