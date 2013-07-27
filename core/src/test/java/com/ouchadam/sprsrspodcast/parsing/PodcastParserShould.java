package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.sprsrspodcast.XMLHelper;
import com.ouchadam.sprsrspodcast.parsing.domain.channel.Channel;
import com.ouchadam.sprsrspodcast.parsing.domain.channel.Image;

import org.junit.Before;
import org.junit.Test;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class PodcastParserShould {

    static final XMLHelper.XML CNET_SMALL = XMLHelper.get(XMLHelper.XmlResource.CNET_SMALL);

    InstigatorResult<Channel> instigator;
    PodcastParser podcastParser;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        ElementFinderFactory finderFactory = SimpleEasyXmlParser.getElementFinderFactory();
        instigator = new PodcastIntigator(finderFactory.getTypeFinder(new ChannelParser(finderFactory)), mock(ParseFinishWatcher.class));
        podcastParser = new PodcastParser(instigator);
    }

    @Test
    public void parse_the_correct_channel_title() throws Exception {
        podcastParser.parse(CNET_SMALL.toInputStream());

        assertThat(instigator.getResult().getTitle()).isEqualTo("CNET UK Podcast");
    }

    @Test
    public void parse_the_correct_channel_category() throws Exception {
        podcastParser.parse(CNET_SMALL.toInputStream());

        assertThat(instigator.getResult().getCategory()).isEqualTo("Technology");
    }

    @Test
    public void parse_the_correct_channel_image() throws Exception {
        podcastParser.parse(CNET_SMALL.toInputStream());

        Image image = instigator.getResult().getImage();
        assertThat(image.getUrl()).isEqualTo("http://www.cnet.co.uk/images/rss/logo-cnet.jpg");
        assertThat(image.getLink()).isEqualTo("http://crave.cnet.co.uk/podcast/");
        assertThat(image.getTitle()).isEqualTo("CNET UK Podcast");
        assertThat(image.getWidth()).isEqualTo(88);
        assertThat(image.getHeight()).isEqualTo(56);
    }

    @Test
    public void parse_the_correct_amount_of_feed_items() throws Exception {
        podcastParser.parse(CNET_SMALL.toInputStream());

        assertThat(instigator.getResult().itemCount()).isEqualTo(5);
    }

}
