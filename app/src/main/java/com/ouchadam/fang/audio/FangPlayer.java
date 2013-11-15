package com.ouchadam.fang.audio;

import android.net.Uri;

import com.ouchadam.fang.domain.PodcastPosition;

import java.io.IOException;

public interface FangPlayer {
    void setSource(Uri uri);
    void play(PodcastPosition podcastPosition);
    void pause();
    void goTo(PodcastPosition podcastPosition);
    void release();
    PodcastPosition getPosition();
    boolean isPrepared();
    Uri getSource();

}
