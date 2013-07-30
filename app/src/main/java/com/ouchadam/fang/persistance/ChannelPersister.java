package com.ouchadam.fang.persistance;

import android.content.ContentResolver;

import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.ChannelMarshaller;

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
