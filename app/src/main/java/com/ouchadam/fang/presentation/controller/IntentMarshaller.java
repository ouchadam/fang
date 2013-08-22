package com.ouchadam.fang.presentation.controller;

import android.content.Intent;

public interface IntentMarshaller<T> {
    Intent to(long itemId, T what);
    T from(Intent intent);
}
