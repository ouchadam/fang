package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

public class ItemToPlaylist {

    private final long itemId;
    private final long downloadId;
    private final int listPosition;

    public static ItemToPlaylist from(Item item, long downloadId, int listPosition) {
        return new ItemToPlaylist(item.getId(), downloadId, listPosition);
    }

    public ItemToPlaylist(long itemId, long downloadId, int listPosition) {
        this.itemId = itemId;
        this.downloadId = downloadId;
        this.listPosition = listPosition;
    }

    public long getItemId() {
        return itemId;
    }

    public long getDownloadId() {
        return downloadId;
    }

    public int getListPosition() {
        return listPosition;
    }
}
