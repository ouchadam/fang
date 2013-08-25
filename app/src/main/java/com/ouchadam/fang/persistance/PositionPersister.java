package com.ouchadam.fang.persistance;

import android.content.ContentResolver;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PlaylistMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PositionMarshaller;

public class PositionPersister {

    private final long itemId;
    private final ContentResolver contentResolver;

    public PositionPersister(long itemId, ContentResolver contentResolver) {
        this.itemId = itemId;
        this.contentResolver = contentResolver;
    }

    public void persist(PodcastPosition position) {
        try {
            new Persister<PodcastPosition>(getExecutor(), getMarshaller()).persist(position);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<PodcastPosition> getMarshaller() {
        return new PositionMarshaller(itemId, new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
