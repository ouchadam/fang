package com.ouchadam.fang.presentation.item;

import com.ouchadam.bookkeeper.watcher.adapter.TypedBaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class TypedListAdapter<T> extends TypedBaseAdapter<T> {

    private List<T> data = new ArrayList<T>();

    public void updateData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public T getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return data.get(position).hashCode();
    }

    public List<T> getAll() {
        return data;
    }

}
