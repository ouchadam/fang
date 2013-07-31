package com.ouchadam.fang.domain.item;

public class Item {

    private static final int INVALID_ID = -1;

    private final String title;
    private final String link;
    private final String pubDate;
    private final Audio audio;
    private final String subtitle;
    private final String summary;
    private final int id;

    public Item(String title, String link, String pubDate, Audio audio, String subtitle, String summary) {
        this(title, link, pubDate, audio, subtitle, summary, INVALID_ID);
    }

    public Item(String title, String link, String pubDate, Audio audio, String subtitle, String summary, int id) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.audio = audio;
        this.subtitle = subtitle;
        this.summary = summary;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public Audio getAudio() {
        return audio;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSummary() {
        return summary;
    }

    public int getId() {
        return id;
    }

    public boolean hasValidId() {
        return INVALID_ID != id;
    }
}
