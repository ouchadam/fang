package com.ouchadam.sprsrspodcast.persistance;

import android.content.ContentProviderOperation;

import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

public class OperationWrapperImpl implements OperationWrapper {

    @Override
    public ContentProviderOperationValues newInsert(Uris uri) {
        return new BuilderWrapper(ContentProviderOperation.newInsert(FangProvider.getUri(uri)));
    }

    public static class BuilderWrapper implements ContentProviderOperationValues {

        private final ContentProviderOperation.Builder builder;

        private BuilderWrapper(ContentProviderOperation.Builder builder) {
            this.builder = builder;
        }

        @Override
        public void withValue(String key, Object value) {
            builder.withValue(key, value);
        }

        public ContentProviderOperation build() {
            return builder.build();
        }
    }

}
