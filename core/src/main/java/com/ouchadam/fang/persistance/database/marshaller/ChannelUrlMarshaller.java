package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class ChannelUrlMarshaller extends BaseMarshaller<String> {

    private final String channelTitle;

    private List<ContentProviderOperationValues> operations;

    public ChannelUrlMarshaller(OperationWrapper operationWrapper, Channel channel) {
        super(operationWrapper);
        this.channelTitle = channel.getTitle();
    }

    @Override
    public List<ContentProviderOperationValues> marshall(String what) {
        operations = Collections.newArrayList();
        insertChannelUrl(what);
        return operations;
    }

    private void insertChannelUrl(String channelUrl) {
        ContentProviderOperationValues itemBuilder = newUpdateFor(Uris.CHANNEL);
        itemBuilder.withSelection(Tables.Channel.CHANNEL_TITLE.name(), new String[] { channelTitle + "=?"});
        itemBuilder.withValue(Tables.Channel.URL.name(), channelUrl);
        operations.add(itemBuilder);
    }

}