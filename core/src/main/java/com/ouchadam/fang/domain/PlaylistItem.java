package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

public class PlaylistItem {

    private final int itemId;

    public static PlaylistItem from(Item item) {
        return new PlaylistItem(item.getId());
    }

    public PlaylistItem(int itemId) {
        this.itemId = itemId;
    }

    public int getItemId() {
        return itemId;
    }
}
