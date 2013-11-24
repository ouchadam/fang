package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;

import org.xml.sax.Attributes;

class EntryParser implements Parser<Entry> {

    private static final String NAMESPACE_IM = "http://itunes.apple.com/rss";

    private static final String TAG_TITLE = "title";
    private static final String TAG_NAME = "name";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_ARTIST = "artist";
    private static final String TAG_SUMMARY = "summary";

    private final ElementFinder<String> titleFinder;
    private final ElementFinder<String> nameFinder;
    private final ElementFinder<String> imageFinder;
    private final ElementFinder<String> artistFinder;
    private final ElementFinder<String> summaryFinder;

    private ParseWatcher<Entry> listener;
    private EntryHolder entryHolder;

    EntryParser(ElementFinderFactory finderFactory) {
        titleFinder = finderFactory.getStringFinder();
        nameFinder = finderFactory.getStringFinder();
        imageFinder = finderFactory.getStringFinder();
        artistFinder = finderFactory.getStringFinder();
        summaryFinder = finderFactory.getStringFinder();
    }

    @Override
    public void parse(Element element, ParseWatcher<Entry> listener) {
        this.listener = listener;
        element.setElementListener(entryListener);
        System.out.println("Entry parser init");
        titleFinder.find(element, TAG_TITLE);
        summaryFinder.find(element, TAG_SUMMARY);
        nameFinder.find(element, NAMESPACE_IM, TAG_NAME);
        imageFinder.find(element, NAMESPACE_IM, TAG_IMAGE);
        artistFinder.find(element, NAMESPACE_IM, TAG_ARTIST);
    }

    private final ElementListener entryListener = new ElementListener() {

        @Override
        public void start(Attributes attributes) {
            System.out.println("Entry parser start");
            entryHolder = new EntryHolder();
        }

        @Override
        public void end() {
            entryHolder.fullTitle = titleFinder.getResult();
            entryHolder.summary = summaryFinder.getResult();
            entryHolder.image = imageFinder.getResult();
            entryHolder.name = nameFinder.getResult();
            entryHolder.artist = artistFinder.getResult();

            listener.onParsed(entryHolder.asEntry());
        }
    };

    private static class EntryHolder {

        private String summary;
        private String name;
        private String fullTitle;
        private String artist;
        private String image;

        Entry asEntry() {
            return new Entry(fullTitle, name, artist, summary, image);
        }
    }

}
