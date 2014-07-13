package com.ouchadam.fang.parsing.podcast;

import com.novoda.notils.java.Collections;
import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.finder.ElementFinder;
import com.novoda.sexp.finder.ElementFinderFactory;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;

import java.util.List;

import org.xml.sax.Attributes;

class CategoryParser implements Parser<List<String>> {

    private static final String NAMESPACE_ITUNES = "http://www.itunes.com/dtds/podcast-1.0.dtd";
    private static final String TAG_CATEGORY = "category";
    private static final String TAG_CATEGORY_VALUE = "text";

    private final ElementFinder<String> categoriesFinder;

    private ParseWatcher<List<String>> listener;
    private CategoryHolder categoryHolder;

    CategoryParser(ElementFinderFactory finderFactory) {
        categoriesFinder = finderFactory.getListElementFinder(new CategoryChildFinder(finderFactory), categoryWatcher);
    }

    private static class CategoryChildFinder implements Parser<String> {

        private final ElementFinder<String> categoryFinder;
        private ParseWatcher<String> listener;

        private CategoryChildFinder(ElementFinderFactory finderFactory) {
            categoryFinder = finderFactory.getStringFinder();
        }

        @Override
        public void parse(Element element, ParseWatcher<String> listener) {
            this.listener = listener;
            element.setElementListener(categoryParsed);
            categoryFinder.find(element, NAMESPACE_ITUNES, TAG_CATEGORY);
        }

        private final ElementListener categoryParsed = new ElementListener() {
            @Override
            public void start(Attributes attributes) {
                listener.onParsed(attributes.getValue(TAG_CATEGORY_VALUE));
            }

            @Override
            public void end() {
            }
        };
    }

    @Override
    public void parse(Element element, ParseWatcher<List<String>> listener) {
        this.listener = listener;
        element.setElementListener(categoryParsed);
        categoriesFinder.find(element, NAMESPACE_ITUNES, TAG_CATEGORY);
    }

    private final ParseWatcher<String> categoryWatcher = new ParseWatcher<String>() {
        @Override
        public void onParsed(String item) {
            categoryHolder.categories.add(item);
        }
    };

    private final ElementListener categoryParsed = new ElementListener() {
        @Override
        public void start(Attributes attributes) {
            categoryHolder = new CategoryHolder();
            categoryHolder.categories.add(attributes.getValue(TAG_CATEGORY_VALUE));
        }

        @Override
        public void end() {
            listener.onParsed(categoryHolder.categories);
        }
    };


    private static class CategoryHolder {
        List<String> categories = Collections.newArrayList();
    }
}
