package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ItemMarshallerShould {

    @Mock
    ContentProviderOperationValues operationValues;

    @Mock
    Item item;

    StubItemMarshaller itemMarshaller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        itemMarshaller = new StubItemMarshaller();
    }

    @Test
    public void add_the_title_to_the_operation_list() throws Exception {
        String title = "title";
        when(item.getTitle()).thenReturn(title);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.TITLE.name(), title);
    }

    @Test
    public void add_the_pubDate_to_the_operation_list() throws Exception {
        String pubDate = "pubDate";
        when(item.getPubDate()).thenReturn(pubDate);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.PUBDATE.name(), pubDate);
    }

    private class StubItemMarshaller extends ItemMarshaller {

        public StubItemMarshaller() {
            super(mock(OperationWrapper.class));
        }

        @Override
        protected ContentProviderOperationValues newInsertFor(Uris uris) {
            return operationValues;
        }
    }

}
