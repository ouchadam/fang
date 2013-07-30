package com.ouchadam.fang.parsing;

import java.io.InputStream;

public interface XmlParser<T> {
    void parse(InputStream inputStream);
    T getResult();
}
