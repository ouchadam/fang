package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

import java.io.Serializable;

public class ItemToPlaylist implements Serializable {

    private final long itemId;
    private final long downloadId;

    private int listPosition;

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

    public int getListPosition() {
        return listPosition;
    }

    public void setListPosition(int listPosition) {
        this.listPosition = listPosition;
    }
}
