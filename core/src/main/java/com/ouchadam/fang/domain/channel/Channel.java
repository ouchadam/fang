package com.ouchadam.fang.domain.channel;

import com.ouchadam.fang.domain.item.Item;

import java.util.List;

public class Channel {

    private final String title;
    private final String category;
    private final Image image;
    private final List<Item> items;
    private final String summary;

    public Channel(String title, String category, Image image, String summary, List<Item> items) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.summary = summary;
        this.items = items;
    }

    public int itemCount() {
        return items.size();
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public Image getImage() {
        return image;
    }

    public String getSummary() {
        return summary;
    }

    public List<Item> getItems() {
        return items;
    }
}