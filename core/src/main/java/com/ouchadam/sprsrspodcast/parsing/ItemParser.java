package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;
import com.ouchadam.sprsrspodcast.parsing.domain.item.Item;

import org.xml.sax.Attributes;

class ItemParser implements Parser<Item> {

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
