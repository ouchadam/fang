package com.ouchadam.fang.parsing;

import com.novoda.notils.java.Collections;
import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;

import java.util.List;

import org.xml.sax.Attributes;

class ChannelParser implements Parser<Channel> {

    private static final String TAG_TITLE = "title";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_ITEM = "item";
    private static final String TAG_SUMMARY = "summary";

    private static final String TAG_NAMESPACE_ITUNES = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    private final ElementFinder<String> titleFinder;
    private final ElementFinder<String> categoryFinder;
    private final ElementFinder<Image> imageFinder;
    private final ElementFinder<Item> itemFinder;
    private final ElementFinder<String> summaryFinder;

    private ParseWatcher<Channel> listener;
    private ChannelHolder channelHolder;

    ChannelParser(ElementFinderFactory finderFactory) {
        titleFinder = finderFactory.getStringFinder();
        categoryFinder = finderFactory.getStringFinder();
        imageFinder = finderFactory.getTypeFinder(new ImageParser(finderFactory));
        summaryFinder = finderFactory.getStringFinder();
        itemFinder = finderFactory.getListElementFinder(new ItemParser(finderFactory), parseWatcher);
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
        summaryFinder.find(element, TAG_NAMESPACE_ITUNES, TAG_SUMMARY);
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
            channelHolder.setCategory(categoryFinder.getResult());
            channelHolder.setImage(imageFinder.getResult());
            channelHolder.setSummary(summaryFinder.getResult());
            listener.onParsed(channelHolder.asChannel());
        }
    };

    private static class ChannelHolder {
        String title = "";
        String category = "";
        Image image = Image.nullSafe();
        String summary = "";
        List<Item> items = Collections.newArrayList();

        Channel asChannel() {
            return new Channel(title, category, image, summary, items);
        }

        public void setCategory(String category) {
            if (category != null) {
                this.category = category;
            }
        }

        public void setImage(Image image) {
            if (image != null) {
                this.image = image;
            }
        }

        public void setSummary(String summary) {
            if (summary != null) {
                this.summary = summary;
            }
        }
    }

}
