package com.ouchadam.fang.presentation.controller;

import android.content.Intent;

public interface IntentMarshaller<T> {
    Intent to(T what);
    T from(Intent intent);
}
