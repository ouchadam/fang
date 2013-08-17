package com.ouchadam.fang.domain.item;

public class ChannelItem {

    private final Item item;
    private final String imageUrl;
    private final String channelTitle;

    public ChannelItem(Item item, String imageUrl, String channelTitle) {
        this.item = item;
        this.imageUrl = imageUrl;
        this.channelTitle = channelTitle;
    }

    public Item getItem() {
        return item;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

}
