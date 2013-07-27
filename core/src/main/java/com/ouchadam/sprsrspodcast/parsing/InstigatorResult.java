package com.ouchadam.sprsrspodcast.parsing;

import com.novoda.sexp.Instigator;

interface InstigatorResult<T> extends Instigator {
    T getResult();
}
