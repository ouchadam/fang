package com.ouchadam.fang.audio.event;

import android.net.Uri;

import com.ouchadam.fang.audio.PlayItem;
import com.ouchadam.fang.domain.PodcastPosition;

public class PlayerEvent {

    private Event event;
    private PodcastPosition position;
    private long id;
    private int playlistPosition;

    private PlayerEvent() {
    }

    public Event getEvent() {
        return event;
    }

    public PodcastPosition getPosition() {
        return position;
    }

    public long getId() {
        return id;
    }

    public int getPlaylistPosition() {
        return playlistPosition;
    }

    public enum Event {
        PLAY,
        PAUSE,
        PLAY_PAUSE,
        STOP,
        GOTO,
        FAST_FORWARD,
        REWIND,
        NEW_SOURCE,
        COMPLETE,
        RESET;

        public static final String ACTION_PREFIX = "com.fang.action.";
        public static final String NOTIFICATION_PREFIX = "com.fang.notification.";

        public String toAction(String prefix) {
            return prefix + toString();
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

        public PlayerEvent newSource(PlayItem playItem) {
            return newSource(playItem.getPlaylistPosition(), "TODO");
        }

        public PlayerEvent newSource(int playlistPosition, String playlistName) {
            playerEvent.event = Event.NEW_SOURCE;
            playerEvent.playlistPosition = playlistPosition;
            return build();
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

        public PlayerEvent reset() {
            playerEvent.event = Event.RESET;
            return build();
        }

        public PlayerEvent playPause() {
            playerEvent.event = Event.PLAY_PAUSE;
            return build();
        }

        public PlayerEvent fastForward() {
            playerEvent.event = Event.FAST_FORWARD;
            return build();
        }

        private PlayerEvent build() {
            return playerEvent;
        }

        public PlayerEvent rewind() {
            playerEvent.event = Event.REWIND;
            return build();
        }

        public PlayerEvent complete() {
            playerEvent.event = Event.COMPLETE;
            return build();
        }
    }

    public static class Builder {

        private final PlayerEvent playerEvent;

        public Builder() {
            this(new PlayerEvent());
        }

        public Builder(PlayerEvent playerEvent) {
            this.playerEvent = playerEvent;
        }

        public Builder withId(long itemId) {
            playerEvent.id = itemId;
            return this;
        }

        public Builder withEvent(Event event) {
            playerEvent.event = event;
            return this;
        }

        public PlayerEvent build() {
            return playerEvent;
        }

    }

}
