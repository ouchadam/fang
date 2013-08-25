package com.ouchadam.fang.presentation.controller;

import android.net.Uri;

import com.ouchadam.fang.domain.PodcastPosition;

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
        GOTO,
        NEW_SOURCE;

        public static final String ACTION_PREFIX = "com.fang.action.";
        public static final String NOTIFICATION_PREFIX = "com.fang.notification.";

        public String toAction() {
            return ACTION_PREFIX + toString();
        }

        public String toNotification() {
            return NOTIFICATION_PREFIX + toString();
        }

        public static Event fromAction(String prefix, String action) {
            if (action.startsWith(prefix)) {
                return valueOf(action.substring(prefix.length()));
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

        public PlayerEvent play() {
            playerEvent.event = Event.PLAY;
            return build();
        }

        public PlayerEvent pause() {
            playerEvent.event = Event.PAUSE;
            return build();
        }

        public PlayerEvent newSource(long itemId, Uri source) {
            playerEvent.event = Event.NEW_SOURCE;
            playerEvent.id = itemId;
            playerEvent.source = validate(source);
            return build();
        }

        private <T> T validate(T what) {
            if (what == null) {
                throw new NullPointerException("Don't pass nulls to the builer!");
            }
            return what;
        }

        public PlayerEvent goTo(PodcastPosition position) {
            playerEvent.event = Event.GOTO;
            playerEvent.position = position;
            return build();
        }

        public PlayerEvent stop() {
            playerEvent.event = Event.STOP;
            return build();
        }

        private PlayerEvent build() {
            return playerEvent;
        }
    }

}
