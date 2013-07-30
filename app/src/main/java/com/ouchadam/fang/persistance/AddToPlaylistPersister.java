package com.ouchadam.fang.persistance;

import android.content.ContentResolver;
import com.ouchadam.fang.domain.PlaylistItem;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PlaylistMarshaller;

public class AddToPlaylistPersister {

    private final ContentResolver contentResolver;

    public AddToPlaylistPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(PlaylistItem playlistItem) {
        try {
            new Persister<PlaylistItem>(getExecutor(), getMarshaller()).persist(playlistItem);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<PlaylistItem> getMarshaller() {
        return new PlaylistMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
