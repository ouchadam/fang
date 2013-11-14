package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import sun.rmi.runtime.Log;

public class ChannelMarshaller extends BaseMarshaller<Channel> {

    private final OperationWrapper operationWrapper;
    private final String channelUrl;
    private int currentItemCount;

    private List<ContentProviderOperationValues> operations;

    public ChannelMarshaller(OperationWrapper operationWrapper, String channelUrl, int currentItemCount) {
        super(operationWrapper);
        this.operationWrapper = operationWrapper;
        this.channelUrl = channelUrl;
        this.currentItemCount = currentItemCount;
    }

    @Override
    public List<ContentProviderOperationValues> marshall(Channel what) {
        operations = Collections.newArrayList();
        insertChannel(what, channelUrl);
        return operations;
    }

    private void insertChannel(Channel channel, String channelUrl) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.CHANNEL);
        itemBuilder.withValue(Tables.Channel.CHANNEL_TITLE.name(), channel.getTitle());
        itemBuilder.withValue(Tables.Channel.CATEGORY.name(), channel.getCategory());
        itemBuilder.withValue(Tables.Channel.SUMMARY.name(), channel.getSummary());
        itemBuilder.withValue(Tables.Channel.URL.name(), channelUrl);

        List<Item> items = channel.getItems();
        List<Item> prunedItems = removeDuplicates(items);
        itemBuilder.withValue(Tables.Channel.NEW_ITEM_COUNT.name(), getNewCount(prunedItems.size(), channel.getTitle()));

        operations.add(itemBuilder);
        buildWithItems(channel.getTitle(), prunedItems);
        buildWithImage(channel.getTitle(), channel.getImage());
    }

    private List<Item> removeDuplicates(List<Item> items) {
        List<Item> prunedList = new ArrayList<Item>();
        prunedList.addAll(new HashSet<Item>(items));
        return prunedList;
    }

    private int getNewCount(int channelItemCount, String channelTitle) {
        int result = channelItemCount - currentItemCount;
        System.out.println("??? : Channel : " + channelTitle + " channel has : " + channelItemCount + " || stored has " + currentItemCount + " new count : "  + result);
        return result < 0 ? 0 : result;
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