package com.ouchadam.fang.persistance;

import android.content.ContentResolver;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.PlaylistMarshaller;

import java.util.List;

public class AddToPlaylistPersister {

    private final ContentResolver contentResolver;

    public AddToPlaylistPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(ItemToPlaylist itemToPlaylist) {
        List<ItemToPlaylist> itemToPlaylists = Collections.newArrayList();
        itemToPlaylists.add(itemToPlaylist);
        persist(itemToPlaylists);
    }

    public void persist(List<ItemToPlaylist> itemToPlaylist) {
        try {
            new Persister<List<ItemToPlaylist>>(getExecutor(), getMarshaller()).persist(itemToPlaylist);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<List<ItemToPlaylist>> getMarshaller() {
        return new PlaylistMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
