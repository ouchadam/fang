package com.ouchadam.fang.parsing;

import com.novoda.sexp.Instigator;

public interface InstigatorResult<T> extends Instigator {
    T getResult();
}
