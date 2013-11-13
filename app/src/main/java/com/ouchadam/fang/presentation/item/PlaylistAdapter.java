package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.ProgressDelegate;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
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
        View view = createView(convertView, viewGroup);
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        ListItemProgress.Stage stage = progressDelegate.getStage(position);
        if (stage == ListItemProgress.Stage.IDLE) {
            updateIdleViewHolder(viewHolder, getItem(position));
        } else {
            progressDelegate.handleDownloadProgress(position, viewHolder);
        }
        setHolderImage(viewHolder, getItem(position).getImageUrl());
        return view;
    }

    private View createView(View convertView, ViewGroup viewGroup) {
        return convertView == null ? createNewView(viewGroup) : convertView;
    }

    private View createNewView(ViewGroup viewGroup) {
        View newView = layoutInflater.inflate(R.layout.download_item_adapter, viewGroup, false);
        initViewTag(newView);
        return newView;
    }

    private void initViewTag(View newView) {
        ViewHolder tag = new ViewHolder();
        initHolder(newView, tag);
        newView.setTag(tag);
    }

    private void initHolder(View newView, ViewHolder tag) {
        tag.progressBar = (ProgressBar) newView.findViewById(R.id.progress);
        tag.title = Views.findById(newView, R.id.text);
        tag.channelTitle = Views.findById(newView, R.id.channel_title);
        tag.channelImage = Views.findById(newView, R.id.channel_image);
        tag.itemTime = Views.findById(newView, R.id.item_time);
        tag.listened = Views.findById(newView, R.id.listened);
    }

    private void updateIdleViewHolder(ViewHolder holder, FullItem item) {
        setHolderText(holder, item);
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
        holder.listened.setText(item.getInitialPlayPosition().asPercentage() + "%" + " listened");
        holder.progressBar.setVisibility(View.GONE);
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
        ProgressBar progressBar;
    }

}