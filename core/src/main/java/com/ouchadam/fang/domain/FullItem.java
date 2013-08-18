package com.ouchadam.fang.domain;

import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;

public class FullItem {

    private final Item item;
    private final String channelTitle;
    private final Image image;
    private final long downloadId;

    public FullItem(Item item, String channelTitle, Image image, long downloadId) {
        this.item = item;
        this.channelTitle = channelTitle;
        this.image = image;
        this.downloadId = downloadId;
    }

    public Item getItem() {
        return item;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public long getDownloadId() {
        return downloadId;
    }
}
