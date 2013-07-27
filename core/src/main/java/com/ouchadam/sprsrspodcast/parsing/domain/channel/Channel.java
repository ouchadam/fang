package com.ouchadam.sprsrspodcast.parsing.domain.channel;

import com.ouchadam.sprsrspodcast.parsing.domain.item.Item;

import java.util.List;

public class Channel {

    private final String title;
    private final String category;
    private final Image image;
    private final List<Item> items;

    public Channel(String title, String category, Image image, List<Item> items) {
        this.title = title;
        this.category = category;
        this.image = image;
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
}
