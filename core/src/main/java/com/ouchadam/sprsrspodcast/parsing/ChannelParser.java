package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.sprsrspodcast.parsing.domain.channel.Channel;
import com.ouchadam.sprsrspodcast.parsing.domain.channel.Image;
import com.ouchadam.sprsrspodcast.parsing.domain.item.Item;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;

class ChannelParser implements Parser<Channel> {

    private static final String TAG_TITLE = "title";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_ITEM = "item";

    private final ElementFinder<String> titleFinder;
    private final ElementFinder<String> categoryFinder;
    private final ElementFinder<Image> imageFinder;
    private final ElementFinder<Item> itemFinder;

    private ParseWatcher<Channel> listener;
    private ChannelHolder channelHolder;

    ChannelParser(ElementFinderFactory finderFactory) {
        titleFinder = finderFactory.getStringFinder();
        categoryFinder = finderFactory.getStringFinder();
        imageFinder = finderFactory.getTypeFinder(new ImageParser(finderFactory));
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
        titleFinder.find(element, TAG_TITLE);
        categoryFinder.find(element, TAG_CATEGORY);
        imageFinder.find(element, TAG_IMAGE);
        itemFinder.find(element, TAG_ITEM);
    }

    private final ElementListener channelListener = new ElementListener() {

        @Override
        public void start(Attributes attributes) {
            channelHolder = new ChannelHolder();
        }

        @Override
        public void end() {
            channelHolder.title = titleFinder.getResult();
            channelHolder.category = categoryFinder.getResult();
            channelHolder.image = imageFinder.getResult();
            listener.onParsed(channelHolder.asChannel());
        }
    };

    private static class ChannelHolder {
        String title;
        String category;
        Image image = Image.nullSafe();
        List<Item> items = new ArrayList<Item>();

        Channel asChannel() {
            return new Channel(title, category, image, items);
        }
    }

}
