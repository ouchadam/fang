package com.ouchadam.fang.parsing.itunesrss;

import com.novoda.sax.Element;
import com.novoda.sax.ElementListener;
import com.novoda.sax.StartElementListener;
import com.novoda.sax.TextElementListener;
import com.novoda.sexp.marshaller.AttributeMarshaller;
import com.novoda.sexp.parser.ParseWatcher;
import com.novoda.sexp.parser.Parser;

import org.xml.sax.Attributes;

public class NamespaceAttributeParser<T> implements Parser<T>, StartElementListener {

    private final AttributeMarshaller<T> attributeMarshaller;
    private final String namespace;
    private final String[] attrs;
    private ParseWatcher<T> listener;

    public NamespaceAttributeParser(AttributeMarshaller<T> attributeMarshaller, String namespace, String... attrs) {
        this.attributeMarshaller = attributeMarshaller;
        this.namespace = namespace;
        this.attrs = attrs;
    }

    @Override
    public void parse(Element element, final ParseWatcher<T> listener) {
        this.listener = listener;
        System.out.println("!!! : " + "namespace init : " + element.toString() + " : ");
        element.setStartElementListener(this);
    }

    @Override
    public void start(Attributes attributes) {
        System.out.println("!!! : " + "namespace atr start : " + attributes.getLength());
        String[] values = getAttributeValues(attributes);
        listener.onParsed(attributeMarshaller.marshall(values));
    }

    private String[] getAttributeValues(Attributes attributes) {
        String[] values = new String[attrs.length];
        for (int i = 0; i < attrs.length; i++) {
            System.out.println("!!! : " + "atr size : " + attributes.getLength());
            values[i] = attributes.getValue(namespace, attrs[i]);
        }
        return values;
    }
}
