package com.ouchadam.fang.persistance;

import android.content.ContentResolver;

import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.persistance.database.Executable;
import com.ouchadam.fang.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.ChannelMarshaller;
import com.ouchadam.fang.persistance.database.marshaller.ChannelUrlMarshaller;

public class ChannelPersister {

    private final ContentResolver contentResolver;

    public ChannelPersister(ContentResolver contentResolver) {
        this.contentResolver = contentResolver;
    }

    public void persist(Channel channel, String channelUrl, int currentItemCount) {
        try {
            new Persister<Channel>(getExecutor(), getChannelMarshaller(channelUrl, currentItemCount)).persist(channel);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<Channel> getChannelMarshaller(String channelUrl, int currentItemCount) {
        return new ChannelMarshaller(new OperationWrapperImpl(), channelUrl, currentItemCount);
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(contentResolver);
    }

}
