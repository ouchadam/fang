package com.ouchadam.fang.persistance.database;

public class Tables {

    private Tables() {
        throw new RuntimeException("This class should not be instantiated");
    }

    public enum Channel {
        _id,
        CHANNEL_TITLE,
        CATEGORY,
        SUMMARY,
        URL,
        NEW_ITEM_COUNT;
    }

    public enum ChannelImage {
        IMAGE_CHANNEL,
        IMAGE_URL,
        LINK,
        TITLE,
        WIDTH,
        HEIGHT;
    }

    public enum Item {
        _id,
        ITEM_CHANNEL,
        TITLE,
        PUBDATE,
        DURATION,
        AUDIO_URL,
        AUDIO_TYPE,
        LINK,
        HERO_IMAGE,
        SUBTITLE,
        SUMMARY,
        UNIQUEID;
    }

    public enum Playlist {
        ITEM_ID,
        DOWNLOADED,
        DOWNLOAD_ID,
        PLAY_POSITION,
        MAX_DURATION,
        LIST_POSITION;
    }

}
