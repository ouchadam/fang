package com.ouchadam.fang.parsing.itunesrss;

class Entry {

    private final String fullTitle;
    private final String name;
    private final String artist;
    private final String summary;
    private final String image;

    public Entry(String fullTitle, String name, String artist, String summary, String image) {
        this.fullTitle = fullTitle;
        this.name = name;
        this.artist = artist;
        this.summary = summary;
        this.image = image;
    }

    public String getName() {
        return name;
    }
}
