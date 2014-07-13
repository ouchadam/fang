package com.ouchadam.fang.domain.item;

import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.FangDuration;

public class Item {

    private static final int INVALID_ID = -1;

    private final String title;
    private final String link;
    private final String heroImage;
    private final FangCalendar pubDate;
    private final FangDuration duration;
    private final Audio audio;
    private final String subtitle;
    private final String summary;
    private final int id;

    public Item(String title, String link, String heroImage, FangCalendar pubDate, FangDuration duration, Audio audio, String subtitle, String summary) {
        this(title, link, heroImage, pubDate, duration, audio, subtitle, summary, INVALID_ID);
    }

    public Item(String title, String link, String heroImage, FangCalendar pubDate, FangDuration duration, Audio audio, String subtitle, String summary, int id) {
        this.title = title;
        this.link = link;
        this.heroImage = heroImage;
        this.pubDate = pubDate;
        this.duration = duration;
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

    public FangCalendar getPubDate() {
        return pubDate;
    }

    public FangDuration getDuration() {
        return duration;
    }

    public Audio getAudio() {
        return audio;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getSummary() {
        if (!has(summary) && has(subtitle)) {
            return subtitle;
        } else {
            return summary;
        }
    }
    private boolean has(String text) {
        return text != null && text.length() != 0;
    }

    public long getId() {
        return id;
    }

    public String getHeroImage() {
        return heroImage;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Item item = (Item) o;

        if (audio != null ? !audio.equals(item.audio) : item.audio != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return audio != null ? audio.hashCode() : 0;
    }
}
