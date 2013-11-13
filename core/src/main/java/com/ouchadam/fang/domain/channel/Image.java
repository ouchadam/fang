package com.ouchadam.fang.domain.channel;

public class Image {

    private final String url;
    private final String link;
    private final String title;
    private final Integer width;
    private final Integer height;

    public static Image nullSafe() {
        return new Image("", "", "", 0, 0);
    }

    public static Image basic(String url) {
        return new Image(url, "", "", 0, 0);
    }

    public Image(String url, String link, String title, Integer width, Integer height) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Image image = (Image) o;

        if (height != null ? !height.equals(image.height) : image.height != null) return false;
        if (link != null ? !link.equals(image.link) : image.link != null) return false;
        if (title != null ? !title.equals(image.title) : image.title != null) return false;
        if (url != null ? !url.equals(image.url) : image.url != null) return false;
        if (width != null ? !width.equals(image.width) : image.width != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (link != null ? link.hashCode() : 0);
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }
}
