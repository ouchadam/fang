package com.ouchadam.sprsrspodcast.persistance.database;

public class Tables {

    private Tables() {
        throw new RuntimeException("This class should not be instantiated");
    }

    public enum Channel {
        TITLE,
        CATEGORY,
        SUMMARY;
    }

    public enum ChannelImage {
        CHANNEL,
        URL,
        LINK,
        TITLE,
        WIDTH,
        HEIGHT;
    }

    public enum Item {
        CHANNEL,
        TITLE,
        PUBDATE,
        AUDIO_URL,
        AUDIO_TYPE,
        LINK,
        SUBTITLE,
        SUMMARY;
    }

}
