package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.sprsrspodcast.domain.channel.Image;

import org.xml.sax.Attributes;

class ImageParser implements Parser<Image> {

    private static final String TAG_URL = "url";
    private static final String TAG_LINK = "link";
    private static final String TAG_TITLE = "title";
    private static final String TAG_WIDTH = "width";
    private static final String TAG_HEIGHT = "height";

    private final ElementFinder<String> urlFinder;
    private final ElementFinder<String> linkFinder;
    private final ElementFinder<String> titleFinder;
    private final ElementFinder<Integer> widthFinder;
    private final ElementFinder<Integer> heightFinder;

    private ParseWatcher<Image> listener;
    private ImageHolder imageHolder;

    ImageParser(ElementFinderFactory finderFactory) {
        urlFinder = finderFactory.getStringFinder();
        linkFinder = finderFactory.getStringFinder();
        titleFinder = finderFactory.getStringFinder();
        widthFinder = finderFactory.getIntegerFinder();
        heightFinder = finderFactory.getIntegerFinder();
    }

    @Override
    public void parse(Element element, ParseWatcher<Image> listener) {
        this.listener = listener;
        element.setElementListener(imageParsed);
        urlFinder.find(element, TAG_URL);
        linkFinder.find(element, TAG_LINK);
        titleFinder.find(element, TAG_TITLE);
        widthFinder.find(element, TAG_WIDTH);
        heightFinder.find(element, TAG_HEIGHT);
    }

    private final ElementListener imageParsed = new ElementListener() {
        @Override
        public void start(Attributes attributes) {
            imageHolder = new ImageHolder();
        }

        @Override
        public void end() {
            imageHolder.url = urlFinder.getResult();
            imageHolder.link = linkFinder.getResult();
            imageHolder.title = titleFinder.getResult();
            imageHolder.setWidth(widthFinder.getResult());
            imageHolder.setHeight(heightFinder.getResult());

            listener.onParsed(imageHolder.asImage());
        }
    };

    private static class ImageHolder {

        String url;
        String link;
        String title;
        Integer width;
        Integer height;

        Image asImage() {
            return new Image(url, link, title, width, height);
        }

        void setWidth(Integer width) {
            this.width = validateInteger(width);
        }

        void setHeight(Integer height) {
            this.height = validateInteger(height);
        }

        private Integer validateInteger(Integer integer) {
            return integer == null ? 0 : integer;
        }

    }
}
