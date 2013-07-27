package com.ouchadam.sprsrspodcast.domain.channel;

public class Image {

    private final String url;
    private final String link;
    private final String title;
    private final int width;
    private final int height;

    public static Image nullSafe() {
        return new Image("", "", "", 0, 0);
    }

    public Image(String url, String link, String title, int width, int height) {
        this.url = url;
        this.link = link;
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public String getUrl() {
        return url;
    }

    public String getLink() {
        return link;
    }

    public String getTitle() {
        return title;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }
}
