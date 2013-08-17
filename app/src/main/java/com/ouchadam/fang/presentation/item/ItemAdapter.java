package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.item.ChannelItem;
import com.ouchadam.fang.domain.item.Item;
import com.squareup.picasso.Picasso;

public class ItemAdapter extends TypedListAdapter<ChannelItem> {

    private final LayoutInflater layoutInflater;
    private final Context context;

    public ItemAdapter(LayoutInflater layoutInflater, Context context) {
        this.layoutInflater = layoutInflater;
        this.context = context;
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
        ChannelItem item = getItem(position);

        updateViewPosition(viewHolder, position);
        updateViewHolder(viewHolder, item);
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
        holder.title = Views.findById(view, R.id.text);
        holder.channelTitle = Views.findById(view, R.id.channel_title);
        holder.channelImage = Views.findById(view, R.id.channel_image);
        holder.position = position;
        return holder;
    }

    private void updateViewPosition(ViewHolder viewHolder, int position) {
        viewHolder.position = position;
    }

    private void updateViewHolder(ViewHolder holder, ChannelItem item) {
        setHolderText(holder, item);
        setHolderImage(holder, item.getImageUrl());
    }

    private void setHolderImage(ViewHolder holder, String imageUrl) {
        if (holder.channelImage != null) {
            Picasso.with(context).load(imageUrl).resize(200, 200).centerCrop().into(holder.channelImage);
        }
    }

    private void setHolderText(ViewHolder holder, ChannelItem item) {
        holder.title.setText(item.getItem().getTitle());
        holder.channelTitle.setText(item.getChannelTitle());
    }

    static class ViewHolder {
        TextView title;
        TextView channelTitle;
        ImageView channelImage;
        int position;
    }

}
