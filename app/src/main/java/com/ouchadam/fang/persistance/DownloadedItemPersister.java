package com.ouchadam.fang.persistance;

import android.content.ContentResolver;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.DownloadedItemMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PlaylistMarshaller;

public class DownloadedItemPersister {

    private final ContentResolver contentResolver;

    public DownloadedItemPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(Long downloadId) {
        try {
            new Persister<Long>(getExecutor(), getMarshaller()).persist(downloadId);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<Long> getMarshaller() {
        return new DownloadedItemMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
