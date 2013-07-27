package com.ouchadam.sprsrspodcast;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.SimpleEasyXmlParser;
import com.novoda.sexp.SimpleTagInstigator;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.sprsrspodcast.parsing.Channel;
import com.ouchadam.sprsrspodcast.parsing.Item;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.xml.sax.Attributes;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;

public class FooShould {

    static final XMLHelper.XML CNET_SMALL = XMLHelper.get(XMLHelper.XmlResource.CNET_SMALL);

    @Mock
    ParseFinishWatcher parseFinishWatcher;

    ChannelParser channelParser;
    StubIntigator stubIntigator;

    @Before
    public void setUp() throws Exception {
        initMocks(this);
        channelParser = new ChannelParser(SimpleEasyXmlParser.getElementFinderFactory());
    }

    @Test
    public void parse_the_correct_amount_of_feed_items() throws Exception {
        ElementFinder<Channel> channelFinder = SimpleEasyXmlParser.getElementFinderFactory().getTypeFinder(channelParser);

        parse(CNET_SMALL, channelFinder);

        assertThat(channelFinder.getResult().itemCount()).isEqualTo(5);
    }

    private void parse(XMLHelper.XML xml, ElementFinder elementFinder) {
        stubIntigator = new StubIntigator(elementFinder, parseFinishWatcher);
        SimpleEasyXmlParser.parse(xml.toInputStream(), stubIntigator);
    }

    private static class StubIntigator extends SimpleTagInstigator {

        private static final String TAG_ROOT = "rss";
        private static final String TAG_CHANNEL = "channel";

        public StubIntigator(ElementFinder<?> elementFinder, ParseFinishWatcher parseFinishWatcher) {
            super(elementFinder, TAG_CHANNEL, parseFinishWatcher);
        }

        @Override
        public String getRootTag() {
            return TAG_ROOT;
        }
    }

    private static class ChannelParser implements Parser<Channel> {

        private static final String TAG_ITEM = "item";

        private ParseWatcher<Channel> listener;
        private ChannelHolder channelHolder;
        private final ElementFinder<Item> itemFinder;

        private ChannelParser(ElementFinderFactory finderFactory) {
            itemFinder = finderFactory.getListElementFinder(new ItemParser(), parseWatcher);
        }

        private final ParseWatcher<Item> parseWatcher = new ParseWatcher<Item>() {
            @Override
            public void onParsed(Item item) {
                channelHolder.items.add(item);
            }
        };

        @Override
        public void parse(Element element, ParseWatcher<Channel> listener) {
            this.listener = listener;
            element.setElementListener(channelListener);
            itemFinder.find(element, TAG_ITEM);
        }

        private final ElementListener channelListener = new ElementListener() {

            @Override
            public void start(Attributes attributes) {
                channelHolder = new ChannelHolder();
            }

            @Override
            public void end() {
                listener.onParsed(channelHolder.asChannel());
            }
        };

        private static class ChannelHolder {
            List<Item> items = new ArrayList<Item>();

            Channel asChannel() {
                return new Channel(items);
            }
        }

        private static class ItemParser implements Parser<Item> {

            private ParseWatcher<Item> listener;
            private ItemHolder itemHolder;

            @Override
            public void parse(Element element, ParseWatcher<Item> listener) {
                this.listener = listener;
                element.setElementListener(itemListener);
            }

            private final ElementListener itemListener = new ElementListener() {

                @Override
                public void start(Attributes attributes) {
                    itemHolder = new ItemHolder();
                }

                @Override
                public void end() {
                    listener.onParsed(itemHolder.asItem());
                }
            };

            private static class ItemHolder {
                Item asItem() {
                    return new Item();
                }
            }

        }

    }
}
