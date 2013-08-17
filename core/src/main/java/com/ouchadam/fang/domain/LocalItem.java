package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.item.Item;

public class LocalItem {

    private final Item item;
    private final long downloadId;

    public LocalItem(Item item, long downloadId) {
        this.item = item;
        this.downloadId = downloadId;
    }

    public Item getItem() {
        return item;
    }

    public long getDownloadId() {
        return downloadId;
    }

}
