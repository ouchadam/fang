package com.ouchadam.fang.presentation.item;

import android.view.View;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.R;

public class ItemManipulator<VH> {

    private final ChildFetcher childFetcher;

    public ItemManipulator(ChildFetcher childFetcher) {
        this.childFetcher = childFetcher;
    }

    public VH getItemViewHolder(int itemIdPosition, int viewId) throws ViewHolderNotFoundException {
        View root = childFetcher.getChildAt(itemIdPosition);
        if (root != null) {
            View viewHolderRoot = root.findViewById(viewId);
            if (root.findViewById(viewId) != null && root.getTag() != null) {
                return Classes.from(viewHolderRoot.getTag());
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
