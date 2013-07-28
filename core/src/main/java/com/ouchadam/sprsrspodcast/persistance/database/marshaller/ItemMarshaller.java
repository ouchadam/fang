package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.sprsrspodcast.domain.item.Audio;
import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class ItemMarshaller extends BaseMarshaller<Item>  {

    private List<ContentProviderOperationValues> operations;

    public ItemMarshaller(OperationWrapper operationWrapper) {
        super(operationWrapper);
    }

    @Override
    public List<ContentProviderOperationValues> marshall(Item what) {
        operations = Collections.newArrayList();
        insertItem(what);
        return operations;
    }

    private void insertItem(Item item) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.ITEM);
        itemBuilder.withValue(Tables.Item.TITLE.name(), item.getTitle());
        itemBuilder.withValue(Tables.Item.PUBDATE.name(), item.getPubDate());
        itemBuilder.withValue(Tables.Item.LINK.name(), item.getLink());
        itemBuilder.withValue(Tables.Item.SUBTITLE.name(), item.getSubtitle());
        itemBuilder.withValue(Tables.Item.SUMMARY.name(), item.getSummary());
        buildWithAudio(itemBuilder, item.getAudio());
        operations.add(itemBuilder);
    }

    private void buildWithAudio(ContentProviderOperationValues itemBuilder, Audio audio) {
        itemBuilder.withValue(Tables.Item.AUDIO_URL.name(), audio.getUrl());
        itemBuilder.withValue(Tables.Item.AUDIO_TYPE.name(), audio.getType());
    }

}