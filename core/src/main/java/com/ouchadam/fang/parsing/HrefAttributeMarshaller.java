package com.ouchadam.fang.parsing;

import com.novoda.sexp.marshaller.AttributeMarshaller;

public class HrefAttributeMarshaller implements AttributeMarshaller<String> {

    public static final String HREF_TAG = "href";

    @Override
    public String marshall(String... strings) {
        return strings[0];
    }

}
