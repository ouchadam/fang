package com.ouchadam.fang.audio.event;

import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.domain.PodcastPosition;

public class PlayerEventInteractionManager {

    private final Broadcaster<PlayerEvent> playerBroadcaster;

    public PlayerEventInteractionManager(Broadcaster<PlayerEvent> playerBroadcaster) {
        this.playerBroadcaster = playerBroadcaster;
    }

    public void pause() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().pause());
    }

    public void play() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().play());
    }

    public void rewind() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().rewind());
    }

    public void fastForward() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().fastForward());
    }
}
