package com.ouchadam.fang.persistance;

import android.content.ContentResolver;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;

import java.util.List;

public class RemoveNewItemCountPersister {

    private final ContentResolver contentResolver;

    public RemoveNewItemCountPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(String channelTitle) {
        try {
            new Persister<String>(getExecutor(), getMarshaller()).persist(channelTitle);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<String> getMarshaller() {
        return new NewCountMarshaller(new OperationWrapperImpl());
    }

    private static class NewCountMarshaller extends BaseMarshaller<String> {

        private List<ContentProviderOperationValues> operations;

        public NewCountMarshaller(OperationWrapper operationWrapper) {
            super(operationWrapper);
        }

        @Override
        public List<ContentProviderOperationValues> marshall(String what) {
            operations = Collections.newArrayList();
            ContentProviderOperationValues itemBuilder = newUpdateFor(Uris.CHANNEL);
            itemBuilder.withSelection(Tables.Channel.CHANNEL_TITLE + "=?", new String[] {what});
            itemBuilder.withValue(Tables.Channel.NEW_ITEM_COUNT.name(), 0);
            operations.add(itemBuilder);
            return operations;
        }
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
