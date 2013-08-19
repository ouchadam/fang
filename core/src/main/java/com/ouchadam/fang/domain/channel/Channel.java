package com.ouchadam.fang.domain.channel;

import com.ouchadam.fang.domain.item.Item;

import java.util.List;

public class Channel {

    private static final int INVALID_ID = -1;

    private final String title;
    private final String category;
    private final Image image;
    private final List<Item> items;
    private final String summary;
    private final int id;

    public Channel(String title, String category, Image image, String summary, List<Item> items) {
        this(title, category, image, summary, items, INVALID_ID);
    }

    public Channel(String title, String category, Image image, String summary, List<Item> items, int id) {
        this.title = title;
        this.category = category;
        this.image = image;
        this.summary = summary;
        this.items = items;
        this.id = id;
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

    public int getId() {
        return id;
    }
}
