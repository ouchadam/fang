package com.ouchadam.fang;

import android.net.Uri;
import com.ouchadam.bookkeeper.domain.Downloadable;
import com.ouchadam.fang.domain.item.Item;

public class ItemDownload implements Downloadable {

    private final String title;
    private final String url;

    public static ItemDownload from(Item item) {
        return new ItemDownload(item.getTitle(), item.getAudio().getUrl());
    }

    ItemDownload(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public String description() {
        return title + " description";
    }

    @Override
    public String fileName() {
        return title;
    }

    @Override
    public Uri url() {
        return Uri.parse(url);
    }

}