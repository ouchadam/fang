package com.ouchadam.fang.presentation.controller;

import android.net.Uri;

import com.ouchadam.fang.audio.PodcastPosition;

public class PlayerEvent {

    private Event event;
    private Uri source;
    private PodcastPosition position;
    private long id;

    private PlayerEvent() {
    }

    public Event getEvent() {
        return event;
    }

    public PodcastPosition getPosition() {
        return position;
    }

    public Uri getSource() {
        return source;
    }

    public long getId() {
        return id;
    }

    public enum Event {
        PLAY,
        PAUSE,
        STOP,
        NEW_SOURCE;
        public static final String ACTION_PREFIX = "com.fang.action.";

        public String toAction() {
            return ACTION_PREFIX + toString();
        }

        public static Event fromAction(String action) {
            if (action.startsWith(ACTION_PREFIX)) {
                return valueOf(action.substring(ACTION_PREFIX.length()));
            }
            throw new RuntimeException("tried to create an event from an invalid action : " + action);
        }

    }

    public static class Factory {

        private final PlayerEvent playerEvent;

        public Factory() {
            playerEvent = new PlayerEvent();
        }

        public PlayerEvent play(PodcastPosition position) {
            playerEvent.event = Event.PLAY;
            playerEvent.position = position;
            return build();
        }

        public PlayerEvent pause() {
            playerEvent.event = Event.PAUSE;
            return build();
        }

        public PlayerEvent newSource(long itemId, Uri source) {
            playerEvent.event = Event.NEW_SOURCE;
            playerEvent.id = itemId;
            playerEvent.source = source;
            return build();
        }

        private PlayerEvent build() {
            return playerEvent;
        }

        public PlayerEvent stop() {
            playerEvent.event = Event.STOP;
            return playerEvent;
        }
    }

}
