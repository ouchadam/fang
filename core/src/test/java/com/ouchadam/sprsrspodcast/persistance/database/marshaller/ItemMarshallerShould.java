package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

import com.ouchadam.sprsrspodcast.domain.item.Audio;
import com.ouchadam.sprsrspodcast.domain.item.Item;
import com.ouchadam.sprsrspodcast.persistance.database.Tables;
import com.ouchadam.sprsrspodcast.persistance.database.Uris;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.sprsrspodcast.persistance.database.bridge.OperationWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class ItemMarshallerShould {

    @Mock
    ContentProviderOperationValues operationValues;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
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

    @Test
    public void add_the_link_to_the_operation_list() throws Exception {
        String link = "link";
        when(item.getLink()).thenReturn(link);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.LINK.name(), link);
    }

    @Test
    public void add_the_subtitle_to_the_operation_list() throws Exception {
        String subtitle = "subtitle";
        when(item.getSubtitle()).thenReturn(subtitle);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.SUBTITLE.name(), subtitle);
    }

    @Test
    public void add_the_summary_to_the_operation_list() throws Exception {
        String summary = "summary";
        when(item.getSummary()).thenReturn(summary);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.SUMMARY.name(), summary);
    }

    @Test
    public void add_the_audio_to_the_operation_list() throws Exception {
        Audio audio = new Audio("url", "type");
        when(item.getAudio()).thenReturn(audio);

        itemMarshaller.marshall(item);

        verify(operationValues).withValue(Tables.Item.AUDIO_URL.name(), audio.getUrl());
        verify(operationValues).withValue(Tables.Item.AUDIO_TYPE.name(), audio.getType());
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
