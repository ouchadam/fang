package com.ouchadam.sprsrspodcast.parsing;

import java.util.List;

public class Channel {

    private final List<Item> items;

    public Channel(List<Item> items) {
        this.items = items;
    }

    public int itemCount() {
        return items.size();
    }
}
