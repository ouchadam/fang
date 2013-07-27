package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.marshaller.AttributeMarshaller;
import com.ouchadam.sprsrspodcast.domain.item.Audio;

class EnclosureAttributeMarshaller implements AttributeMarshaller<Audio> {

    private static final String TAG_ATTR_URL = "url";
    private static final String TAG_ATTR_TYPE = "type";

    private static final int ATTR_URL_INDEX = 0;
    private static final int ATTR_TYPE_INDEX = 1;

    private static final String[] attributes;

    static {
        attributes = new String[2];
        attributes[ATTR_URL_INDEX] = TAG_ATTR_URL;
        attributes[ATTR_TYPE_INDEX] = TAG_ATTR_TYPE;
    }

    @Override
    public Audio marshall(String... input) {
        validate(input);
        return new Audio(input[ATTR_URL_INDEX], input[ATTR_TYPE_INDEX]);
    }

    private void validate(String[] input) {
        if (input.length != attributes.length) {
            throw new RuntimeException("Item parser expected 2 attributes but got : " + input.length);
        }
    }

    public static String[] getAttributes() {
        return attributes;
    }
}
