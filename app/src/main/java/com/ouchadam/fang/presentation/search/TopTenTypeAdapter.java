package com.ouchadam.fang.presentation.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.parsing.itunesrss.Entry;
import com.ouchadam.fang.presentation.item.TypedListAdapter;
import com.ouchadam.fang.presentation.topten.TopTenType;
import com.squareup.picasso.Picasso;

class TopTenTypeAdapter extends TypedListAdapter<TopTenType> {

    private final LayoutInflater layoutInflater;

    public TopTenTypeAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return getItemView(position, view, viewGroup);
    }

    private View getItemView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = createAdapterView(layoutInflater, position, viewGroup);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        TopTenType type = TopTenType.values()[position];

        updateViewPosition(viewHolder, position);
        updateViewHolder(viewHolder, type);
        return view;
    }

    private View createAdapterView(LayoutInflater inflater, int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_adapter, parent, false);
        initView(view, position);
        return view;
    }

    private void initView(View view, int position) {
        view.setTag(createViewHolder(view, position));
    }

    private ViewHolder createViewHolder(View view, int position) {
        ViewHolder holder = new ViewHolder();
        holder.channelTitle = Views.findById(view, R.id.text);
        holder.position = position;
        return holder;
    }

    private void updateViewPosition(ViewHolder viewHolder, int position) {
        viewHolder.position = position;
    }

    private void updateViewHolder(ViewHolder holder, TopTenType type) {
        setHolderText(holder, type.name());
    }

    private void setHolderText(ViewHolder holder, String type) {
        holder.channelTitle.setText(type);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).hashCode();
    }

    static class ViewHolder {
        TextView channelTitle;
        int position;
    }
}
