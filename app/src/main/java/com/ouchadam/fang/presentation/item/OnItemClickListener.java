package com.ouchadam.fang.presentation.item;

public interface OnItemClickListener<T> {
    void onItemClick(TypedListAdapter<T> adapter, int position, long itemId);
}
