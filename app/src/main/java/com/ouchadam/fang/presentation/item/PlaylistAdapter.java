package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.graphics.drawable.Drawable;
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

public class PlaylistAdapter extends TypedListAdapter<FullItem> implements ListItemWatcher.ItemWatcher, ItemNotifier.WatcherUpdate<FullItem, PlaylistAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final OnFastMode<FullItem> onFastModeListener;
    private final ProgressDelegate<ViewHolder> progressDelegate;
    private final ItemNotifier<FullItem, ViewHolder> itemNotifier;

    public PlaylistAdapter(LayoutInflater layoutInflater, Context context, OnFastMode<FullItem> onFastModeListener, ItemManipulator<ViewHolder> itemManipulator) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.onFastModeListener = onFastModeListener;
        this.progressDelegate = new PlaylistItemProgressManager(this);
        this.itemNotifier = new ItemNotifier<FullItem, ViewHolder>(itemManipulator, this, this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = createView(convertView, viewGroup);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        PositionHolder positionHolder = (PositionHolder) viewHolder.fastMode.getTag();
        positionHolder.position = position;

        ListItemProgress.Stage stage = progressDelegate.getStage(position);
        FullItem item = getItem(position);
        updateViewHolder(position, viewHolder, stage, item);
        setHolderImage(viewHolder, item.getImageUrl());
        return view;
    }

    private void updateViewHolder(int position, ViewHolder viewHolder, ListItemProgress.Stage stage, FullItem item) {
        if (stage == ListItemProgress.Stage.IDLE) {
            updateIdleViewHolder(viewHolder, item);
        } else {
            progressDelegate.handleDownloadProgress(position, viewHolder);
        }
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
        tag.fastMode = Views.findById(newView, R.id.channel_image_holder);
        tag.fastMode.setTag(new PositionHolder());
        tag.fastMode.setOnClickListener(onClickListener);
    }

    private final View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (onFastModeListener.isEnabled()) {
                PositionHolder position = (PositionHolder) view.getTag();
                FullItem item = getItem(position.position);
                onFastModeListener.onFastMode(item);
            }
        }
    };

    private void updateIdleViewHolder(ViewHolder holder, FullItem item) {
        setHolderText(holder, item);
    }

    private void setHolderText(ViewHolder holder, FullItem item) {
        holder.title.setText(item.getItem().getTitle());
        holder.channelTitle.setText(item.getChannelTitle());
        holder.itemTime.setText(item.getItem().getPubDate().getTimeAgo());
        holder.listened.setText(item.getInitialPlayPosition().asPercentage() + "%" + " listened");
        holder.progressBar.setVisibility(View.GONE);
    }

    private void setHolderImage(ViewHolder holder, String imageUrl) {
        if (holder.channelImage != null) {
            Picasso.with(context).load(imageUrl).resize(200, 200).centerCrop().into(holder.channelImage);
        }
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
    public void notifyItem(long itemId, ListItemProgress.Stage stage) {
        itemNotifier.notifyItem(itemId, R.id.playlist_adapter_container, stage);
    }

    @Override
    public void onWatcherUpdate(int position, ViewHolder viewHolder, ListItemProgress.Stage stage, FullItem item) {
        updateViewHolder(position, viewHolder, stage, item);
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
        View fastMode;
        ImageView channelImage;
        ProgressBar progressBar;
    }

    private static class PositionHolder {
        int position;
    }

}