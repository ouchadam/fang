package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

public class ItemToPlaylist {

    private final long itemId;
    private final long downloadId;

    public static ItemToPlaylist from(Item item, long downloadId) {
        return new ItemToPlaylist(item.getId(), downloadId);
    }

    public ItemToPlaylist(long itemId, long downloadId) {
        this.itemId = itemId;
        this.downloadId = downloadId;
    }

    public long getItemId() {
        return itemId;
    }

    public long getDownloadId() {
        return downloadId;
    }
}
