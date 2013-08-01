package com.ouchadam.fang;

import android.os.Environment;
import com.ouchadam.bookkeeper.Downloadable;
import com.ouchadam.fang.domain.item.Item;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

public class ItemDownload implements Downloadable {

    private final String title;
    private final String url;

    public static ItemDownload from(Item item) {
        return new ItemDownload(item.getTitle(), item.getAudio().getUrl());
    }

    public ItemDownload(String title, String url) {
        this.title = title;
        this.url = url;
    }

    @Override
    public String title() {
        return title;
    }

    @Override
    public File file() {
        return createFile(title());
    }

    private File createFile(String filename) {
        File SDCardRoot = Environment.getExternalStorageDirectory();
        File file = new File(SDCardRoot, filename);
        if (file.exists()) {
            file.delete();
        }
        return file;
    }

    @Override
    public URL url() {
        return getUrl();
    }

    private URL getUrl() {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("Bad url");
        }
    }
}
