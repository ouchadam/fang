package com.ouchadam.fang.persistance.database.marshaller;

public interface Marshaller<From, To> {
    To marshall(From what);
}
