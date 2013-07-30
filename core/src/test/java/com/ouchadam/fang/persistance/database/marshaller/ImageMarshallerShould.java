package com.ouchadam.fang.persistance.database.marshaller;

import com.ouchadam.fang.domain.channel.Image;
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

public class ImageMarshallerShould {

    @Mock
    ContentProviderOperationValues operationValues;

    @Mock
    OperationWrapper operationWrapper;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    Image image;

    ImageMarshaller itemMarshaller;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        itemMarshaller = createImageMarshall("");
    }

    private ImageMarshaller createImageMarshall(String channelTitle) {
        when(operationWrapper.newInsert(any(Uris.class))).thenReturn(operationValues);
        return new ImageMarshaller(operationWrapper, channelTitle);
    }

    @Test
    public void use_the_correct_uri() throws Exception {
        itemMarshaller.marshall(image);

        verify(operationWrapper).newInsert(Uris.IMAGE);
    }

    @Test
    public void add_the_channel_to_the_operation_list() throws Exception {
        String channel = "channel";

        itemMarshaller = createImageMarshall(channel);
        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.CHANNEL.name(), channel);
    }

    @Test
    public void add_the_title_to_the_operation_list() throws Exception {
        String title = "title";
        when(image.getTitle()).thenReturn(title);

        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.TITLE.name(), title);
    }

    @Test
    public void add_the_url_to_the_operation_list() throws Exception {
        String url = "url";
        when(image.getUrl()).thenReturn(url);

        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.URL.name(), url);
    }

    @Test
    public void add_the_link_to_the_operation_list() throws Exception {
        String link = "link";
        when(image.getLink()).thenReturn(link);

        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.LINK.name(), link);
    }

    @Test
    public void add_the_width_to_the_operation_list() throws Exception {
        Integer width = 100;
        when(image.getWidth()).thenReturn(width);

        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.WIDTH.name(), width);
    }

    @Test
    public void add_the_height_to_the_operation_list() throws Exception {
        Integer height = 100;
        when(image.getHeight()).thenReturn(height);

        itemMarshaller.marshall(image);

        verify(operationValues).withValue(Tables.ChannelImage.HEIGHT.name(), height);
    }

}
