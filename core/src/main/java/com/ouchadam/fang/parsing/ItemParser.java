package com.ouchadam.fang.parsing;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.FangDuration;
import com.ouchadam.fang.domain.item.Audio;
import com.ouchadam.fang.domain.item.Item;

import org.xml.sax.Attributes;

class ItemParser implements Parser<Item> {

    private static final String NAMESPACE_ITUNES = "http://www.itunes.com/dtds/podcast-1.0.dtd";

    private static final String TAG_TITLE = "title";
    private static final String TAG_LINK = "link";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_PUBDATE = "pubDate";
    private static final String TAG_DURATION = "duration";
    private static final String TAG_ENCLOSURE = "enclosure";
    private static final String TAG_SUBTITLE = "subtitle";
    private static final String TAG_SUMMARY = "summary";

    private final ElementFinder<String> titleFinder;
    private final ElementFinder<String> linkFinder;
    private final ElementFinder<String> heroFinder;
    private final ElementFinder<String> pubDateFinder;
    private final ElementFinder<String> durationFinder;
    private final ElementFinder<Audio> audioFinder;
    private final ElementFinder<String> subtitleFinder;
    private final ElementFinder<String> summaryFinder;

    private ParseWatcher<Item> listener;
    private ItemHolder itemHolder;

    ItemParser(ElementFinderFactory finderFactory) {
        titleFinder = finderFactory.getStringFinder();
        linkFinder = finderFactory.getStringFinder();
        heroFinder = finderFactory.getAttributeFinder(new HrefAttributeMarshaller(), HrefAttributeMarshaller.HREF_TAG);
        pubDateFinder = finderFactory.getStringFinder();
        durationFinder = finderFactory.getStringFinder();
        audioFinder = finderFactory.getAttributeFinder(new EnclosureAttributeMarshaller(), EnclosureAttributeMarshaller.getAttributes());
        subtitleFinder = finderFactory.getStringFinder();
        summaryFinder = finderFactory.getStringFinder();
    }

    @Override
    public void parse(Element element, ParseWatcher<Item> listener) {
        this.listener = listener;
        element.setElementListener(itemListener);
        titleFinder.find(element, TAG_TITLE);
        linkFinder.find(element, TAG_LINK);
        heroFinder.find(element, NAMESPACE_ITUNES, TAG_IMAGE);
        pubDateFinder.find(element, TAG_PUBDATE);
        durationFinder.find(element, NAMESPACE_ITUNES, TAG_DURATION);
        audioFinder.find(element, TAG_ENCLOSURE);
        subtitleFinder.find(element,NAMESPACE_ITUNES, TAG_SUBTITLE);
        summaryFinder.find(element, NAMESPACE_ITUNES, TAG_SUMMARY);
    }

    private final ElementListener itemListener = new ElementListener() {

        @Override
        public void start(Attributes attributes) {
            itemHolder = new ItemHolder();
        }

        @Override
        public void end() {
            itemHolder.title = titleFinder.getResult();
            itemHolder.link = linkFinder.getResult();
            itemHolder.heroImage = heroFinder.getResult();
            itemHolder.pubDate = pubDateFinder.getResult();
            itemHolder.duration = durationFinder.getResult();
            itemHolder.audio = audioFinder.getResult();
            itemHolder.subtitle = subtitleFinder.getResult();
            itemHolder.summary = summaryFinder.getResult();

            listener.onParsed(itemHolder.asItem());
        }
    };

    private static class ItemHolder {

        private String title;
        private String link;
        private String heroImage;
        private String pubDate;
        private String duration;
        private Audio audio;
        private String subtitle;
        private String summary;

        Item asItem() {
            return new Item(title, link, heroImage, new FangCalendar(pubDate), new FangDuration(duration), audio, subtitle, summary);
        }
    }

}
