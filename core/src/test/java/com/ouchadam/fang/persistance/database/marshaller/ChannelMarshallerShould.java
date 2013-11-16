package com.ouchadam.fang.persistance.database.marshaller;

import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.persistance.database.bridge.ContentProviderOperationValues;
import com.ouchadam.fang.persistance.database.bridge.OperationWrapper;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.Mock;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class ChannelMarshallerShould {

    @Mock
    ContentProviderOperationValues operationValues;

    @Mock
    OperationWrapper operationWrapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Channel channel;

    ChannelMarshaller channelMarshaller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        when(operationWrapper.newInsert(any(Uris.class))).thenReturn(operationValues);
        channelMarshaller = new ChannelMarshaller(operationWrapper, "Channel url", 0);
    }

    @Test
    public void use_the_correct_uri() throws Exception {
        channelMarshaller.marshall(channel);

        verify(operationWrapper).newInsert(Uris.CHANNEL);
    }

    @Test
    public void add_the_title_to_the_operation_list() throws Exception {
        String title = "title";

        when(channel.getTitle()).thenReturn(title);
        channelMarshaller.marshall(channel);

        verify(operationValues).withValue(Tables.Channel.CHANNEL_TITLE.name(), title);
    }

    @Test
    public void add_the_summary_to_the_operation_list() throws Exception {
        String summary = "summary";

        when(channel.getSummary()).thenReturn(summary);
        channelMarshaller.marshall(channel);

        verify(operationValues).withValue(Tables.Channel.SUMMARY.name(), summary);
    }

    @Test
    public void add_the_category_to_the_operation_list() throws Exception {
        String category = "category";

        when(channel.getCategory()).thenReturn(category);
        channelMarshaller.marshall(channel);

        verify(operationValues).withValue(Tables.Channel.CATEGORY.name(), category);
    }

}
