package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

public class ItemToPlaylist {

    private final int itemId;
    private final long downloadId;

    public static ItemToPlaylist from(Item item, long downloadId) {
        return new ItemToPlaylist(item.getId(), downloadId);
    }

    public ItemToPlaylist(int itemId, long downloadId) {
        this.itemId = itemId;
        this.downloadId = downloadId;
    }

    public int getItemId() {
        return itemId;
    }

    public long getDownloadId() {
        return downloadId;
    }
}
