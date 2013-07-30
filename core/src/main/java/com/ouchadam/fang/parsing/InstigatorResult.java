package com.ouchadam.fang.parsing;

import com.novoda.sexp.Instigator;

interface InstigatorResult<T> extends Instigator {
    T getResult();
}
