package com.ouchadam.fang.persistance;

import android.content.ContentResolver;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PlaylistMarshaller;

public class AddToPlaylistPersister {

    private final ContentResolver contentResolver;

    public AddToPlaylistPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(ItemToPlaylist itemToPlaylist) {
        try {
            new Persister<ItemToPlaylist>(getExecutor(), getMarshaller()).persist(itemToPlaylist);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<ItemToPlaylist> getMarshaller() {
        return new PlaylistMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
