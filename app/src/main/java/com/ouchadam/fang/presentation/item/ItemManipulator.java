package com.ouchadam.fang.presentation.item;

import android.view.View;

import com.ouchadam.fang.R;

class ItemManipulator {

    private final ChildFetcher childFetcher;

    public ItemManipulator(ChildFetcher childFetcher) {
        this.childFetcher = childFetcher;
    }

    public PlaylistAdapter.ViewHolder getItemViewHolder(int itemIdPosition) throws ViewHolderNotFoundException {
        View root = childFetcher.getChildAt(itemIdPosition);
        if (root != null && root.findViewById(R.id.playlist_adapter_container) != null) {
            View viewHolderRoot = root.findViewById(R.id.playlist_adapter_container);
            if (root.getTag() != null && root.getTag() instanceof PlaylistAdapter.ViewHolder) {
                return (PlaylistAdapter.ViewHolder) viewHolderRoot.getTag();
            }
        }
        throw new ViewHolderNotFoundException("ViewHolder was not found, prepare the backup plan");
    }

    static class ViewHolderNotFoundException extends Exception {
        ViewHolderNotFoundException(String detailMessage) {
            super(detailMessage);
        }
    }

}
