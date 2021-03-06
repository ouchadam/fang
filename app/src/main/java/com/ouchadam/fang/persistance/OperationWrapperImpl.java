package com.ouchadam.fang.persistance;

import android.content.ContentProviderOperation;

import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

public class OperationWrapperImpl implements OperationWrapper {

    @Override
    public ContentProviderOperationValues newInsert(Uris uri) {
        return new BuilderWrapper(ContentProviderOperation.newInsert(FangProvider.getUri(uri)));
    }

    @Override
    public ContentProviderOperationValues newUpdate(Uris uri) {
        return new BuilderWrapper(ContentProviderOperation.newUpdate(FangProvider.getUri(uri)));
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

        @Override
        public void withSelection(String selection, String[] selectionArgs) {
            builder.withSelection(selection, selectionArgs);
        }

        public ContentProviderOperation build() {
            return builder.build();
        }
    }

}
