package com.ouchadam.fang.presentation.controller;

import android.graphics.drawable.Drawable;
import android.widget.SeekBar;
import android.widget.TextView;

import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.PodcastPosition;

import java.util.concurrent.TimeUnit;

class PositionController {

    private final SeekBar seekBar;
    private final TextView currentTime;
    private final TextView endTime;

    PositionController(SeekBar seekBar, TextView currentTime, TextView endTime) {
        this.seekBar = seekBar;
        this.currentTime = currentTime;
        this.endTime = endTime;
    }

    public void update(boolean positionChanging, PodcastPosition position) {
        seekBar.setMax(position.getDuration());
        if (!positionChanging) {
            seekBar.setProgress(position.value());
        }
        currentTime.setText(toTimeString(position.value()));
        endTime.setText(toTimeString(position.getDuration()));
    }

    private String toTimeString(long millis) {
        return String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
    }

    public void setSeekChangedListener(SeekBar.OnSeekBarChangeListener seekListener) {
        seekBar.setOnSeekBarChangeListener(seekListener);
    }
}
