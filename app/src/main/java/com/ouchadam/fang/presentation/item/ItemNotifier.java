package com.ouchadam.fang.presentation.item;

import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.TypedBaseAdapter;
import com.ouchadam.fang.Log;

class ItemNotifier<T, VH> {

    private final ItemManipulator<VH> itemManipulator;
    private final TypedBaseAdapter<T> baseAdapter;
    private final WatcherUpdate<T, VH> watcherUpdate;

    ItemNotifier(ItemManipulator<VH> itemManipulator, TypedBaseAdapter<T> baseAdapter, WatcherUpdate<T, VH> watcherUpdate) {
        this.itemManipulator = itemManipulator;
        this.baseAdapter = baseAdapter;
        this.watcherUpdate = watcherUpdate;
    }

    public interface WatcherUpdate<T, VH> {
        void onWatcherUpdate(int position, VH viewHolder, ListItemProgress.Stage stage, T item);
    }

    public void notifyItem(long itemId, int viewId, ListItemProgress.Stage stage) {
        try {
            handleWatcherUpdate(itemId, viewId, stage);
        } catch (ItemManipulator.ViewHolderNotFoundException e) {
            Log.e("Tried to update via download watcher but the view holder is not available");
        }
    }

    private void handleWatcherUpdate(long itemId, int viewId, ListItemProgress.Stage stage) throws ItemManipulator.ViewHolderNotFoundException {
        int position = getPositionFor(itemId);
        if (position != -1) {
            VH viewHolder = getItemViewHolder(position, viewId);
            T item = baseAdapter.getItem(position);
            watcherUpdate.onWatcherUpdate(position, viewHolder, stage, item);
        }
    }

    private VH getItemViewHolder(int position, int viewId) throws ItemManipulator.ViewHolderNotFoundException {
        return itemManipulator.getItemViewHolder(position, viewId);
    }

    private int getPositionFor(long itemId) {
        for (int index = 0; index < baseAdapter.getCount(); index++) {
            if (baseAdapter.getItemId(index) == itemId) {
                return index;
            }
        }

        return -1;
    }

    public VH getViewHolder(long itemId, int viewId) throws ItemManipulator.ViewHolderNotFoundException {
        int position = getPositionFor(itemId);
        if (position != -1) {
            return itemManipulator.getItemViewHolder(position, viewId);
        }
        throw new ItemManipulator.ViewHolderNotFoundException("ViewHolder was not found, prepare the backup plan");
    }

}
