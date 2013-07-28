package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.sprsrspodcast.domain.channel.Channel;
import com.ouchadam.sprsrspodcast.parsing.helper.XMLHelper;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class PodcastParserShould {

    static final XMLHelper.XML XML = XMLHelper.get(XMLHelper.XmlResource.HSW_SMALL);

    XmlParser<Channel> podcastParser;

    @Before
    public void setUp()  {
        initMocks(this);
        ElementFinderFactory finderFactory = SimpleEasyXmlParser.getElementFinderFactory();
        InstigatorResult <Channel> instigator = new PodcastIntigator(finderFactory.getTypeFinder(new ChannelParser(finderFactory)), mock(ParseFinishWatcher.class));
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
