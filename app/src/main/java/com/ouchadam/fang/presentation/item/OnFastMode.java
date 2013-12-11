package com.ouchadam.fang.presentation.item;

public interface OnFastMode<T> {
    void onFastMode(T what);
    boolean isPlaying(long itemId);
    boolean isEnabled();
}
