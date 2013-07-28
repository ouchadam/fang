package com.ouchadam.sprsrspodcast.persistance.database.bridge;

import com.ouchadam.sprsrspodcast.persistance.database.Uris;

public interface OperationWrapper {
    ContentProviderOperationValues newInsert(Uris uri);
}
