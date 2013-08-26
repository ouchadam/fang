package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.novoda.notils.android.Views;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.ProgressDelegate;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.squareup.picasso.Picasso;

public class PlaylistAdapter extends TypedListAdapter<FullItem> implements ListItemWatcher.ItemWatcher {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final ProgressDelegate<ViewHolder> progressDelegate;

    public PlaylistAdapter(LayoutInflater layoutInflater, Context context) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.progressDelegate = new ItemProgressManager(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = createView(convertView, viewGroup, position);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        progressDelegate.handleDownloadProgress(position, viewHolder);
        ListItemProgress.Stage stage = progressDelegate.getStage(position);
        if (stage == ListItemProgress.Stage.IDLE) {
            updateViewHolder(viewHolder, getItem(position));
        }
        return view;
    }

    private View createView(View convertView, ViewGroup viewGroup, int position) {
        return convertView == null ? createNewView(viewGroup, position) : convertView;
    }

    private View createNewView(ViewGroup viewGroup, int position) {
        View newView = layoutInflater.inflate(R.layout.download_item_adapter, viewGroup, false);
        initViewTag(newView, position);
        return newView;
    }

    private void initViewTag(View newView, int position) {
        ViewHolder tag = new ViewHolder();
        initHolder(newView, tag, position);
        newView.setTag(tag);
    }

    private void initHolder(View newView, ViewHolder tag, int position) {
        tag.progressBar = (ProgressBar) newView.findViewById(R.id.progress);
        tag.title = Views.findById(newView, R.id.text);
        tag.channelTitle = Views.findById(newView, R.id.channel_title);
        tag.channelImage = Views.findById(newView, R.id.channel_image);
        tag.itemTime = Views.findById(newView, R.id.item_time);
        tag.listened = Views.findById(newView, R.id.listened);
        tag.position = position;

    }

    private void updateViewHolder(ViewHolder holder, FullItem item) {
        setHolderText(holder, item);
        setHolderImage(holder, item.getImageUrl());
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
        holder.listened.setText(item.getInitialPlayPosition().asPercentage() +"%" + " listened");
    }

    @Override
    public void setStageFor(long itemId, ListItemProgress.Stage stage) {
        progressDelegate.setStageFor(itemId, stage);
    }

    @Override
    public void updateProgressValuesFor(long itemId, ProgressValues progressValues) {
        progressDelegate.updateProgressValuesFor(itemId, progressValues);

    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getItemId();
    }

    public static class ViewHolder {
        TextView title;
        TextView channelTitle;
        TextView itemTime;
        TextView listened;
        ImageView channelImage;
        int position;
        ProgressBar progressBar;
    }

}