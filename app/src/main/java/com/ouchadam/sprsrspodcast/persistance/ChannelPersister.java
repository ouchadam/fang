package com.ouchadam.sprsrspodcast.persistance;

import android.content.ContentResolver;

import com.ouchadam.sprsrspodcast.domain.channel.Channel;
import com.ouchadam.sprsrspodcast.persistance.database.Executable;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.ChannelMarshaller;

public class ChannelPersister {

    private final ContentResolver contentResolver;

    public ChannelPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(Channel channel) {
        try {
            new Persister<Channel>(getExecutor(), getChannelMarshaller()).persist(channel);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<Channel> getChannelMarshaller() {
        return new ChannelMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
