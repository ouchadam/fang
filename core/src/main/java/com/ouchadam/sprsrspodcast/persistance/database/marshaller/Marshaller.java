package com.ouchadam.sprsrspodcast.persistance.database.marshaller;

public interface Marshaller<From, To> {
    To marshall(From what);
}
