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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (id != item.id) return false;
        if (audio != null ? !audio.equals(item.audio) : item.audio != null) return false;
        if (link != null ? !link.equals(item.link) : item.link != null) return false;
        if (pubDate != null ? !pubDate.equals(item.pubDate) : item.pubDate != null) return false;
        if (subtitle != null ? !subtitle.equals(item.subtitle) : item.subtitle != null) return false;
        if (summary != null ? !summary.equals(item.summary) : item.summary != null) return false;
        if (title != null ? !title.equals(item.title) : item.title != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = title != null ? title.hashCode() : 0;
        return result;
    }
}
