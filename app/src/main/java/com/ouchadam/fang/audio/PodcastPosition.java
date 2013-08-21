package com.ouchadam.fang.audio;

import java.io.Serializable;

public class PodcastPosition implements Serializable {

    private final int position;

    public PodcastPosition(int position) {
        this.position = position;
    }
}
