package com.ouchadam.fang.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.item.Audio;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class ItemMarshaller extends BaseMarshaller<Item>  {

    private final String channel;

    private List<ContentProviderOperationValues> operations;

    public ItemMarshaller(OperationWrapper operationWrapper, String channel) {
        super(operationWrapper);
        this.channel = channel;
    }

    @Override
    public List<ContentProviderOperationValues> marshall(Item what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(Item item) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.ITEM);
        itemBuilder.withValue(Tables.Item.CHANNEL.name(), channel);
        itemBuilder.withValue(Tables.Item.TITLE.name(), item.getTitle());
        itemBuilder.withValue(Tables.Item.PUBDATE.name(), stringDateToLong(item.getPubDate()));
        itemBuilder.withValue(Tables.Item.LINK.name(), item.getLink());
        itemBuilder.withValue(Tables.Item.SUBTITLE.name(), item.getSubtitle());
        itemBuilder.withValue(Tables.Item.SUMMARY.name(), item.getSummary());
        buildWithAudio(itemBuilder, item.getAudio());
        operations.add(itemBuilder);
    }

    private long stringDateToLong(String date) {
        return new FangCalendar(date).getTimeInMillis();
    }

    private void buildWithAudio(ContentProviderOperationValues itemBuilder, Audio audio) {
        itemBuilder.withValue(Tables.Item.AUDIO_URL.name(), audio.getUrl());
        itemBuilder.withValue(Tables.Item.AUDIO_TYPE.name(), audio.getType());
    }

}