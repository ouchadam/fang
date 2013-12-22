package com.ouchadam.fang.api.model;

import com.google.api.client.util.Key;

public class Feed {

    @Key
    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
