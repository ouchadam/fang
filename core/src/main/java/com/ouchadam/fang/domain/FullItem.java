package com.ouchadam.fang.domain;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;

public class FullItem {

    private final Item item;
    private final String channelTitle;
    private final Image image;
    private final long downloadId;
    private final boolean isDownloaded;
    private final PodcastPosition playPosition;
    private final int playlistPosition;

    public FullItem(Item item, String channelTitle, Image image, long downloadId, boolean downloaded, PodcastPosition playPosition, int playlistPosition) {
        this.item = item;
        this.channelTitle = channelTitle;
        this.image = image;
        this.downloadId = downloadId;
        isDownloaded = downloaded;
        this.playPosition = playPosition;
        this.playlistPosition = playlistPosition;
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

    public String getImageUrl() {
        return image.getUrl();
    }

    public long getItemId() {
        return item.getId();
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public PodcastPosition getInitialPlayPosition() {
        return playPosition;
    }

    public boolean isListenedTo() {
        return playPosition.isHeard(PodcastPosition.MAX_PERCENT);
    }

    public int getPlaylistPosition() {
        return playlistPosition;
    }
}
