package com.ouchadam.fang.persistance;

import android.content.*;
import android.os.RemoteException;

import com.ouchadam.fang.persistance.database.Executable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ContentProviderOperationExecutable implements Executable<List<ContentProviderResult>, List<ContentProviderOperation>> {

    private final ContentResolver resolver;

    public ContentProviderOperationExecutable(ContentResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public List<ContentProviderResult> execute(List<ContentProviderOperation> what) throws ExecutionFailure {
        ContentProviderClient client = null;
        try {
            client = getContentProviderClient();
            return Arrays.asList(client.applyBatch(new ArrayList<ContentProviderOperation>(what)));
        } catch (OperationApplicationException e) {
            throw new ExecutionFailure(e);
        } catch (RemoteException e) {
            throw new ExecutionFailure(e);
        } finally {
            if (client != null) {
                client.release();
            }
        }
    }

    private ContentProviderClient getContentProviderClient() {
        return resolver.acquireContentProviderClient(FangProvider.BASE_URI);
    }

    public ContentResolver getContentResolver() {
        return resolver;
    }
}
