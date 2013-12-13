package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.ProgressDelegate;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.squareup.picasso.Picasso;

public class ItemAdapter extends TypedListAdapter<FullItem> implements ListItemWatcher.ItemWatcher, ItemNotifier.WatcherUpdate<FullItem,ItemAdapter.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final Context context;
    private final OnFastMode<FullItem> onFastModeListener;
    private final ItemProgressManager progressDelegate;
    private final ItemNotifier<FullItem, ViewHolder> itemNotifier;

    public ItemAdapter(LayoutInflater layoutInflater, Context context, OnFastMode<FullItem> onFastModeListener, ItemManipulator<ViewHolder> itemManipulator) {
        this.layoutInflater = layoutInflater;
        this.context = context;
        this.onFastModeListener = onFastModeListener;
        this.progressDelegate = new ItemProgressManager(this);
        this.itemNotifier = new ItemNotifier<FullItem, ViewHolder>(itemManipulator, this, this);
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return getItemView(position, view, viewGroup);
    }

    private View getItemView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = createAdapterView(layoutInflater, viewGroup);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        PositionHolder positionHolder = (PositionHolder) viewHolder.fastModeContainer.getTag();
        positionHolder.position = position;

        FullItem item = getItem(position);
        ListItemProgress.Stage stage = progressDelegate.getStage(position);
        updateViewHolder(position, viewHolder, stage, item);
        setHolderTextColour(viewHolder, item);
        setHolderImage(viewHolder, item);
        setIndicator(viewHolder, item);
        return view;
    }

    private View createAdapterView(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_adapter, parent, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        view.setTag(createViewHolder(view));
    }

    private ViewHolder createViewHolder(View view) {
        ViewHolder holder = new ViewHolder();
        holder.title = Views.findById(view, R.id.text);
        holder.indicator = Views.findById(view, R.id.item_indicator);
        holder.channelTitle = Views.findById(view, R.id.channel_title);
        holder.channelImage = Views.findById(view, R.id.channel_image);
        holder.itemTime = Views.findById(view, R.id.item_time);
        holder.fastModeContainer = Views.findById(view, R.id.fast_mode_container);
        holder.fastModeContainer.setTag(new PositionHolder());
        holder.fastModeContainer.setOnClickListener(onFastModeItemClick);
        return holder;
    }

    private final View.OnClickListener onFastModeItemClick = new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            if (onFastModeListener.isEnabled()) {
                PositionHolder position = (PositionHolder) view.getTag();
                FullItem item = getItem(position.position);
                onFastModeListener.onFastMode(item);
            }
        }
    };

    private void updateViewHolder(int position, ViewHolder viewHolder, ListItemProgress.Stage stage, FullItem item) {
        if (stage == ListItemProgress.Stage.IDLE) {
            updateIdleViewHolder(viewHolder, item);
        } else {
            progressDelegate.handleDownloadProgress(position, viewHolder);
        }
    }


    private void updateIdleViewHolder(ViewHolder holder, FullItem item) {
        setHolderText(holder, item);
    }

    private void setHolderTextColour(ViewHolder holder, FullItem item) {
        holder.title.setTextColor(getTitleColour(item.isListenedTo()));
        holder.channelTitle.setTextColor(getChannelColour(item.isListenedTo()));
        holder.itemTime.setTextColor(getAscentColour(item.isListenedTo()));
    }

    private int getTitleColour(boolean isListenedTo) {
        return isListenedTo ? getColour(R.color.listened_grey) : getColour(android.R.color.primary_text_light);
    }

    private int getChannelColour(boolean listenedTo) {
        return listenedTo ? getColour(R.color.listened_grey) : getColour(android.R.color.darker_gray);
    }

    private void setHolderImage(ViewHolder holder, final FullItem fullItem) {
        String imageUrl = fullItem.getImageUrl();
        if (holder.channelImage != null && isValid(imageUrl)) {
            if (fullItem.isListenedTo()) {
                holder.channelImage.setAlpha(0.5f);
                Picasso.with(context).load(imageUrl).transform(new GreyscaleTransformation(imageUrl)).resize(200, 200).centerCrop().into(holder.channelImage);
            } else {
                holder.channelImage.setAlpha(1f);
                Picasso.with(context).load(imageUrl).resize(200, 200).centerCrop().into(holder.channelImage);
            }
        }
    }

    private boolean isValid(String url) {
        return url != null && !url.isEmpty();
    }


    private void setHolderText(ViewHolder holder, FullItem item) {
        holder.title.setText(item.getItem().getTitle());
        holder.channelTitle.setText(item.getChannelTitle());
        holder.itemTime.setText(item.getItem().getPubDate().getTimeAgo());
    }

    private int getAscentColour(boolean isListenedTo) {
        return isListenedTo ? getColour(R.color.listened_grey) : getColour(R.color.fang_ascent);
    }

    private void setIndicator(ViewHolder holder, FullItem item) {
        if (item.isListenedTo()) {
            holder.indicator.setBackgroundColor(getColour(R.color.listened_grey));
        } else {
            if (item.isDownloaded()) {
                holder.indicator.setBackgroundColor(getColour(R.color.fang_ascent));
            } else {
                holder.indicator.setBackgroundColor(getColour(android.R.color.transparent));
            }
        }
    }

    private int getColour(int colourId) {
        return context.getResources().getColor(colourId);
    }

    @Override
    public long getItemId(int position) {
        return getItem(position).getItem().getId();
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
        itemNotifier.notifyItem(itemId, R.id.item_adapter_container, stage);
    }

    @Override
    public void onWatcherUpdate(int position, ViewHolder viewHolder, ListItemProgress.Stage stage, FullItem item) {
        updateViewHolder(position, viewHolder, stage, item);
    }

    public static class ViewHolder {
        View indicator;
        TextView title;
        TextView channelTitle;
        TextView itemTime;
        ImageView channelImage;
        View fastModeContainer;
    }

    private static class PositionHolder {
        int position;
    }


}
