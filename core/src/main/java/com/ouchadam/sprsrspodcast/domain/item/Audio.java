package com.ouchadam.sprsrspodcast.domain.item;

public class Audio {

    private final String url;
    private final String type;

    public Audio(String url, String type) {
        this.url = url;
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public String getType() {
        return type;
    }
}
