package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.novoda.notils.java.Collections;
import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

import java.util.List;

public class ItemMarshaller extends BaseMarshaller implements Marshaller<Item, List<ContentProviderOperationValues>> {

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

    private void insertItem(com.ouchadam.sprsrspodcast.domain.item.Item item) {
        ContentProviderOperationValues itemBuilder = newInsertFor(Uris.ITEM);
        itemBuilder.withValue(Tables.Item.TITLE.name(), item.getTitle());
        itemBuilder.withValue(Tables.Item.PUBDATE.name(), item.getPubDate());
        operations.add(itemBuilder);
    }

}
