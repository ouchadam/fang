package com.ouchadam.fang.parsing.podcast;

import com.novoda.notils.java.Collections;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.parsing.InstigatorResult;
import com.ouchadam.fang.parsing.XmlParser;
import com.ouchadam.fang.parsing.helper.XMLHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Collection;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

@RunWith(Parameterized.class)
public class PodcastParserShould {

    final XMLHelper.XML XML;

    XmlParser<Channel> podcastParser;

    public PodcastParserShould(XMLHelper.XML XML) {
        this.XML = XML;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> xmls = Collections.newArrayList();
        xmls.add(new Object[] {XMLHelper.get(XMLHelper.XmlResource.CNET_SMALL) });
        xmls.add(new Object[] {XMLHelper.get(XMLHelper.XmlResource.HSW_SMALL) });
        return xmls;
    }

    @Before
    public void setUp()  {
        initMocks(this);
        InstigatorResult<Channel> instigator = new PodcastInstigator(ChannelFinder.newInstance(), mock(ParseFinishWatcher.class));
        podcastParser = new PodcastParser(instigator);
    }

    @Test
    public void parse_the_correct_channel_title()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getTitle()).isEqualTo(XML.values().channelTitle());
    }

    @Test
    public void parse_the_correct_channel_category()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getCategory()).isEqualTo(XML.values().channelCategory());
    }

    @Test
    public void parse_the_correct_channel_image()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getImage()).isEqualTo(XML.values().channelImage());
    }

    @Test
    public void parse_the_correct_channel_itunes_summary()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getSummary()).isEqualTo(XML.values().channelSummary());
    }

    @Test
    public void parse_the_correct_amount_of_feed_items()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().itemCount()).isEqualTo(XML.values().itemCount());
    }

    @Test
    public void parse_the_correct_first_item_title()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getTitle()).isEqualTo(XML.values().firstItemTitle());
    }

    @Test
    public void parse_the_correct_first_item_link()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getLink()).isEqualTo(XML.values().firstItemLink());
    }

    @Test
    public void parse_the_correct_first_item_pubDate()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getPubDate()).isEqualTo(XML.values().firstItemPubDate());
    }

    @Test
    public void parse_the_correct_first_item_audio()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getAudio()).isEqualTo(XML.values().firstItemAudio());
    }

    @Test
    public void parse_the_correct_first_item_subtitle()  {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getSubtitle()).isEqualTo(XML.values().firstItemSubtitle());
    }

    @Test
    public void parse_the_correct_first_item_summary() {
        podcastParser.parse(XML.toInputStream());

        assertThat(podcastParser.getResult().getItems().get(0).getSummary()).isEqualTo(XML.values().firstItemSummary());
    }

}
