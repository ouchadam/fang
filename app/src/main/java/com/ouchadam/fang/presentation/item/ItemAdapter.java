package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.squareup.picasso.Picasso;

public class ItemAdapter extends TypedListAdapter<FullItem> {

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
        FullItem item = getItem(position);

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
        holder.indicator = Views.findById(view, R.id.item_indicator);
        holder.channelTitle = Views.findById(view, R.id.channel_title);
        holder.channelImage = Views.findById(view, R.id.channel_image);
        holder.itemTime = Views.findById(view, R.id.item_time);
        holder.position = position;
        return holder;
    }

    private void updateViewPosition(ViewHolder viewHolder, int position) {
        viewHolder.position = position;
    }

    private void updateViewHolder(ViewHolder holder, FullItem item) {
        setHolderText(holder, item);
        setHolderImage(holder, item.getImageUrl());
        setIndicator(holder, item);
    }

    private void setHolderImage(ViewHolder holder, String imageUrl) {
        if (holder.channelImage != null) {
            Picasso.with(context).load(imageUrl).resize(200, 200).centerCrop().into(holder.channelImage);
        }
    }

    private void setHolderText(ViewHolder holder, FullItem item) {
        holder.title.setText(item.getItem().getTitle());
        holder.channelTitle.setText(item.getChannelTitle());
        holder.itemTime.setText(item.getItem().getPubDate().getTimeAgo());
    }

    private void setIndicator(ViewHolder holder, FullItem item) {
        if (item.isDownloaded()) {
            holder.indicator.setBackgroundColor(context.getResources().getColor(R.color.fang_ascent));
        } else {
            holder.indicator.setBackground(null);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getItem().getId();
    }

    static class ViewHolder {
        View indicator;
        TextView title;
        TextView channelTitle;
        TextView itemTime;
        ImageView channelImage;
        int position;
    }

}
