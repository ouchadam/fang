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

public class ChannelMarshaller extends BaseMarshaller<Channel> {

    private final OperationWrapper operationWrapper;

    private List<ContentProviderOperationValues> operations;

    public ChannelMarshaller(OperationWrapper operationWrapper) {
        super(operationWrapper);
        this.operationWrapper = operationWrapper;
    }

    @Override
    public List<ContentProviderOperationValues> marshall(Channel what) {
        operations = Collections.newArrayList();
        insertChannel(what);
        return operations;
    }

    private void insertChannel(Channel channel) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.CHANNEL);
        itemBuilder.withValue(Tables.Channel.TITLE.name(), channel.getTitle());
        itemBuilder.withValue(Tables.Channel.CATEGORY.name(), channel.getCategory());
        itemBuilder.withValue(Tables.Channel.SUMMARY.name(), channel.getSummary());
        operations.add(itemBuilder);

        buildWithItems(channel.getTitle(), channel.getItems());
        buildWithImage(channel.getTitle(), channel.getImage());
    }

    private void buildWithItems(String channelTitle, List<Item> items) {
        ItemMarshaller itemMarshaller = new ItemMarshaller(operationWrapper, channelTitle);
        for (Item item : items) {
            operations.addAll(itemMarshaller.marshall(item));
        }
    }

    private void buildWithImage(String channelTitle, Image image) {
        ImageMarshaller imageMarshaller = new ImageMarshaller(operationWrapper, channelTitle);
        operations.addAll(imageMarshaller.marshall(image));
    }

}