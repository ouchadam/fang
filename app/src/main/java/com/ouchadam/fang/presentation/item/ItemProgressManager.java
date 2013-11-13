package com.ouchadam.fang.presentation.item;

import android.util.Log;
import android.view.View;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.TypedBaseAdapter;
import com.ouchadam.fang.domain.FullItem;

class ItemProgressManager extends ListItemProgress<FullItem, PlaylistAdapter.ViewHolder> {

    public ItemProgressManager(TypedBaseAdapter<FullItem> baseAdapter) {
        super(baseAdapter);
    }

    @Override
    protected void onStart(FullItem what, PlaylistAdapter.ViewHolder viewHolder) {
        viewHolder.title.setText("Download about to start");
        viewHolder.progressBar.setProgress(0);
        viewHolder.progressBar.setMax(100);
        viewHolder.progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onUpdate(FullItem what, PlaylistAdapter.ViewHolder viewHolder, ProgressValues progressValues) {
        viewHolder.title.setText("Downloading...");
        viewHolder.listened.setText(progressValues.getPercentage() + "%");
        viewHolder.channelTitle.setText(what.getItem().getTitle());
        viewHolder.progressBar.setVisibility(View.VISIBLE);
        viewHolder.progressBar.setMax(100);
        viewHolder.progressBar.setProgress(progressValues.getPercentage());
    }

    @Override
    protected void onStop(FullItem what, PlaylistAdapter.ViewHolder viewHolder) {
        viewHolder.title.setText(what.getItem().getTitle());
        viewHolder.channelTitle.setText(what.getChannelTitle());
        viewHolder.listened.setText(what.getInitialPlayPosition().asPercentage() +"%" + " listened");
        viewHolder.progressBar.setVisibility(View.GONE);
    }

}