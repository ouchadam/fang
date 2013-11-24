package com.ouchadam.fang.parsing.itunesrss;

public class Entry {

    private final String fullTitle;
    private final String name;
    private final String artist;
    private final String summary;
    private final String imageUrl;

    public Entry(String fullTitle, String name, String artist, String summary, String imageUrl) {
        this.fullTitle = fullTitle;
        this.name = name;
        this.artist = artist;
        this.summary = summary;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getFullTitle() {
        return fullTitle;
    }

    public String getArtist() {
        return artist;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
