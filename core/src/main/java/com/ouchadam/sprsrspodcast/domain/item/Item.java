package com.ouchadam.sprsrspodcast.domain.item;

public class Item {

    private final String title;
    private final String link;
    private final String pubDate;
    private final Audio audio;
    private final String subtitle;
    private final String summary;

    public Item(String title, String link, String pubDate, Audio audio, String subtitle, String summary) {
        this.title = title;
        this.link = link;
        this.pubDate = pubDate;
        this.audio = audio;
        this.subtitle = subtitle;
        this.summary = summary;
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

}
