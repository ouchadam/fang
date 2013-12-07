package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.notils.java.Collections;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.parsing.InstigatorResult;
import com.ouchadam.fang.parsing.XmlParser;
import com.ouchadam.fang.parsing.helper.XMLHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.MockitoAnnotations.initMocks;

public class TopPodcastParserShould {

    XmlParser<TopPodcastFeed> topFeedParser;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        List<Object[]> xmls = Collections.newArrayList();
        xmls.add(new Object[]{XMLHelper.get(XMLHelper.XmlResource.CNET_SMALL)});
        xmls.add(new Object[]{XMLHelper.get(XMLHelper.XmlResource.HSW_SMALL)});
        return xmls;
    }

    @Before
    public void setUp() {
        initMocks(this);
        TopPodcastFeedParser topPodcastFeedParser = new TopPodcastFeedParser();
        InstigatorResult<TopPodcastFeed> instigator = new TopPodcastInstigator(TopPodcastFeedFinder.newInstance(topPodcastFeedParser), topPodcastFeedParser, mock(ParseFinishWatcher.class));
        topFeedParser = new TopPodcastParser(instigator);
    }

    @Test
    public void parse_the_entries_from_top_rss() {
        topFeedParser.parse(new ByteArrayInputStream(topPodcastsXml.getBytes()));

        List<Entry> entries = topFeedParser.getResult().getEntries();
        assertThat(entries).isNotEmpty();
    }

    @Test
    public void parse_the_correct_title_id() {
        topFeedParser.parse(new ByteArrayInputStream(topPodcastsXml.getBytes()));

        List<Entry> entries = topFeedParser.getResult().getEntries();
        assertThat(entries.get(0).getFullTitle()).isEqualTo("Mastertapes - BBC Radio 4");
    }


    @Test
    public void parse_the_correct_id() {
        topFeedParser.parse(new ByteArrayInputStream(topPodcastsXml.getBytes()));

        List<Entry> entries = topFeedParser.getResult().getEntries();
        assertThat(entries.get(0).getId()).isEqualTo("test_id_attr");
    }


    //language=XML
    private final String topPodcastsXml =
            "<feed xmlns:im=\"http://itunes.apple.com/rss\" xmlns=\"http://www.w3.org/2005/Atom\" xml:lang=\"en\">\n" +
                "<entry>\n" +
                "<id im:id=\"test_id_attr\">" + "test_id" + "</id>\n" +
                "<updated>2013-11-24T11:34:45-07:00</updated>\n" +
                "<title>Mastertapes - BBC Radio 4</title>\n" +
                "<summary>\n" +
                "John Wilson interviews leading musicians about the creation of an album they feel is one of their most significant of their career.\n" +
                "</summary>\n" +
                "<im:name>Mastertapes</im:name>\n" +
                "<link rel=\"alternate\" type=\"text/html\" href=\"https://itunes.apple.com/gb/podcast/mastertapes/id577459979?mt=2&amp;uo=2\"/>\n" +
                "<im:contentType term=\"Podcast\" label=\"Podcast\"/>\n" +
                "<im:artist href=\"https://itunes.apple.com/gb/artist/bbc/id121676617?mt=2&amp;uo=2\">BBC Radio 4</im:artist>\n" +
                "<im:price amount=\"0\" currency=\"GBP\">Free</im:price>\n" +
                "<im:image height=\"55\">\n" +
                "http://a1366.phobos.apple.com/us/r30/Podcasts/v4/85/ce/3b/85ce3b2a-800a-c1f5-915b-42fa9f37c4f3/mza_839175886520777275.55x55-70.jpg\n" +
                "</im:image>\n" +
                "<im:image height=\"60\">\n" +
                "http://a88.phobos.apple.com/us/r30/Podcasts/v4/85/ce/3b/85ce3b2a-800a-c1f5-915b-42fa9f37c4f3/mza_839175886520777275.60x60-50.jpg\n" +
                "</im:image>\n" +
                "<im:image height=\"170\">\n" +
                "http://a1351.phobos.apple.com/us/r30/Podcasts/v4/85/ce/3b/85ce3b2a-800a-c1f5-915b-42fa9f37c4f3/mza_839175886520777275.170x170-75.jpg\n" +
                "</im:image>\n" +
                "<rights>© (C) BBC 2013</rights>\n" +
                "<im:releaseDate label=\"19 November 2013\">2013-11-19T07:30:00-07:00</im:releaseDate>\n" +
                "<content type=\"html\">\n" +
                "<table border=\"0\" width=\"100%\"> <tr> <td> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tr valign=\"top\" align=\"left\"> <td align=\"center\" width=\"166\" valign=\"top\"> <a href=\"https://itunes.apple.com/gb/podcast/mastertapes/id577459979?mt=2&amp;uo=2\"><img border=\"0\" alt=\"BBC Radio 4 - Mastertapes artwork\" src=\"http://a1351.phobos.apple.com/us/r30/Podcasts/v4/85/ce/3b/85ce3b2a-800a-c1f5-915b-42fa9f37c4f3/mza_839175886520777275.170x170-75.jpg\" /></a> </td> <td width=\"10\"><img alt=\"\" width=\"10\" height=\"1\" src=\"https://s.mzstatic.com/images/spacer.gif\" /></td> <td width=\"95%\"> <b><a href=\"https://itunes.apple.com/gb/podcast/mastertapes/id577459979?mt=2&amp;uo=2\">Mastertapes</a></b><br/> <a href=\"https://itunes.apple.com/gb/artist/bbc/id121676617?mt=2&amp;uo=2\">BBC Radio 4</a> <font size=\"2\" face=\"Helvetica,Arial,Geneva,Swiss,SunSans-Regular\"> <br/> <b>Genre:</b> <a href=\"https://itunes.apple.com/gb/genre/podcasts-music/id1310?mt=2&amp;uo=2\">Music</a> <br/> <b>Release Date:</b> 19 November 2013 </font> </td> </tr> </table> </td> </tr> <tr> <td> <font size=\"2\" face=\"Helvetica,Arial,Geneva,Swiss,SunSans-Regular\"><br/>John Wilson interviews leading musicians about the creation of an album they feel is one of their most significant of their career.</font><br/> <font size=\"2\" face=\"Helvetica,Arial,Geneva,Swiss,SunSans-Regular\"> &#169; © (C) BBC 2013</font> </td> </tr> </table>\n" +
                "</content>\n" +
                "</entry>\n" +
            "</feed>";

}
