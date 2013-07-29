package com.ouchadam.sprsrspodcast.presentation.item;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class TypedListAdapter<T> extends BaseAdapter {

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

}
