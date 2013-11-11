package com.ouchadam.fang.domain;

import java.io.Serializable;

public class PodcastPosition implements Serializable {

    private int currentPosition;
    private int duration;

    private static PodcastPosition DEFAULT = new PodcastPosition(0, 100);

    public static PodcastPosition idle() {
        return DEFAULT;
    }

    public PodcastPosition(int currentPosition, int duration) {
        this.currentPosition = currentPosition;
        this.duration = duration;
    }

    public int value() {
        return currentPosition;
    }

    public int asPercentage() {
        float percentCoeff = (float) currentPosition / (float) duration;
        return (int) ((percentCoeff * 100) + 0.5F);
    }

    public int getDuration() {
        return duration;
    }

    public boolean isIdle() {
        return DEFAULT.equals(this);
    }

    public boolean isCompleted() {
        return currentPosition == duration;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PodcastPosition that = (PodcastPosition) o;

        if (currentPosition != that.currentPosition) return false;
        if (duration != that.duration) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = currentPosition;
        result = 31 * result + duration;
        return result;
    }
}
