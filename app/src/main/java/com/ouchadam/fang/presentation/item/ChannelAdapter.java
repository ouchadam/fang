package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.channel.Channel;
import com.squareup.picasso.Picasso;

public class ChannelAdapter extends TypedListAdapter<Channel> {

    private final Context context;
    private final LayoutInflater layoutInflater;

    public ChannelAdapter(Context context, LayoutInflater layoutInflater) {
        this.context = context;
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
        Channel item = getItem(position);

        updateViewPosition(viewHolder, position);
        updateViewHolder(viewHolder, item);
        return view;
    }

    private View createAdapterView(LayoutInflater inflater, int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.channel_adapter, parent, false);
        initView(view, position);
        return view;
    }

    private void initView(View view, int position) {
        view.setTag(createViewHolder(view, position));
    }

    private ViewHolder createViewHolder(View view, int position) {
        ViewHolder holder = new ViewHolder();
        holder.title = Views.findById(view, R.id.channel_text);
        holder.newCount = Views.findById(view, R.id.channel_new_count);
        holder.newCountContainer = Views.findById(view, R.id.new_count_container);
        holder.image = Views.findById(view, R.id.channel_image);
        holder.position = position;
        return holder;
    }

    private void updateViewPosition(ViewHolder viewHolder, int position) {
        viewHolder.position = position;
    }

    private void updateViewHolder(ViewHolder holder, Channel channel) {
        setHolderText(holder, channel);
        setHolderImage(holder, channel.getImage().getUrl());
    }

    private void setHolderText(ViewHolder holder, Channel channel) {
        holder.title.setText(channel.getTitle());
        if (channel.getNewItemCount() > 0) {
            setNewCountVisibility(holder, View.VISIBLE);
            holder.newCount.setText(channel.getNewItemCount() + "!");
        } else {
            setNewCountVisibility(holder, View.INVISIBLE);
        }
    }

    private void setNewCountVisibility(ViewHolder holder, int visibility) {
        holder.newCount.setVisibility(visibility);
        holder.newCountContainer.setVisibility(visibility);
    }

    private void setHolderImage(ViewHolder holder, String imageUrl) {
        if (holder.image != null) {
            Picasso.with(context).load(imageUrl).fit().into(holder.image);
        }
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getId();
    }

    static class ViewHolder {
        TextView title;
        TextView newCount;
        View newCountContainer;
        ImageView image;
        int position;
    }

}
