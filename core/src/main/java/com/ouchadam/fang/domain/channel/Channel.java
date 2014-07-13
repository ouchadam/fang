package com.ouchadam.fang.domain.channel;

import com.ouchadam.fang.domain.item.Item;

import java.util.List;

public class Channel {

    private static final int INVALID_ID = -1;
    private static final int NO_COUNT = 0;

    private final String title;
    private final String category;
    private final List<String> categories;
    private final Image image;
    private final int newItemCount;
    private final List<Item> items;
    private final String summary;
    private final int id;

    public Channel(String title, String category, List<String> categories, Image image, String summary, List<Item> items) {
        this(title, category, categories, image, summary, NO_COUNT, items, INVALID_ID);
    }

    public Channel(String title, String category, List<String> categories, Image image, String summary, int newItemCount, List<Item> items, int id) {
        this.title = title;
        this.category = category;
        this.categories = categories;
        this.image = image;
        this.summary = summary;
        this.newItemCount = newItemCount;
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

    public int getNewItemCount() {
        return newItemCount;
    }

    public List<String> getCategories() {
        return categories;
    }
}
