package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.marshaller.AttributeMarshaller;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;

import org.xml.sax.Attributes;

class EntryParser implements Parser<Entry> {

    private static final String NAMESPACE_IM = "http://itunes.apple.com/rss";
    public static final String NAMESPACE = "http://www.w3.org/2005/Atom";

    private static final String TAG_TITLE = "title";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_IMAGE = "image";
    private static final String TAG_ARTIST = "artist";
    private static final String TAG_SUMMARY = "summary";

    private final ElementFinder<String> titleFinder;
    private final ElementFinder<String> idFinder;
    private final ElementFinder<String> nameFinder;
    private final ElementFinder<String> imageFinder;
    private final ElementFinder<String> artistFinder;
    private final ElementFinder<String> summaryFinder;

    private ParseWatcher<Entry> listener;
    private EntryHolder entryHolder;

    EntryParser(ElementFinderFactory finderFactory) {
        titleFinder = finderFactory.getStringFinder();
        idFinder = finderFactory.getTypeFinder(new NamespaceAttributeParser<String>(new Foo(), NAMESPACE_IM, TAG_ID));
        nameFinder = finderFactory.getStringFinder();
        imageFinder = finderFactory.getStringFinder();
        artistFinder = finderFactory.getStringFinder();
        summaryFinder = finderFactory.getStringFinder();
    }

    private static class Foo implements AttributeMarshaller<String> {
        @Override
        public String marshall(String... input) {
            return input[0];
        }
    }

    @Override
    public void parse(Element element, ParseWatcher<Entry> listener) {
        this.listener = listener;
        element.setElementListener(entryListener);

        idFinder.find(element, NAMESPACE, TAG_ID);
        titleFinder.find(element, NAMESPACE, TAG_TITLE);
        summaryFinder.find(element, NAMESPACE, TAG_SUMMARY);
        nameFinder.find(element, NAMESPACE_IM, TAG_NAME);
        imageFinder.find(element, NAMESPACE_IM, TAG_IMAGE);
        artistFinder.find(element, NAMESPACE_IM, TAG_ARTIST);
    }

    private final ElementListener entryListener = new ElementListener() {

        @Override
        public void start(Attributes attributes) {
            entryHolder = new EntryHolder();
        }

        @Override
        public void end() {
            entryHolder.id = idFinder.getResult();
            entryHolder.fullTitle = titleFinder.getResult();
            entryHolder.summary = summaryFinder.getResult();
            entryHolder.image = imageFinder.getResult();
            entryHolder.name = nameFinder.getResult();
            entryHolder.artist = artistFinder.getResult();

            listener.onParsed(entryHolder.asEntry());
        }
    };

    private static class EntryHolder {

        private String id;
        private String summary;
        private String name;
        private String fullTitle;
        private String artist;
        private String image;

        Entry asEntry() {
            return new Entry(id, fullTitle, name, artist, summary, image);
        }
    }

}
