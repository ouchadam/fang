package com.ouchadam.fang.audio.event;

import com.ouchadam.fang.Broadcaster;
import com.ouchadam.fang.domain.PodcastPosition;

public class PlayerEventInteractionManager {

    private final Broadcaster<PlayerEvent> playerBroadcaster;
    private PlayerEvent sourceEvent;
    private PlayerEvent goToEvent;

    public PlayerEventInteractionManager(Broadcaster<PlayerEvent> playerBroadcaster) {
        this.playerBroadcaster = playerBroadcaster;
    }

    public void setData(PlayerEvent source, PlayerEvent goTo) {
        this.sourceEvent = source;
        this.goToEvent = goTo;
    }

    public void pause() {
        playerBroadcaster.broadcast(new PlayerEvent.Factory().pause());
    }

    public void play(PodcastPosition podcastPosition) {
        validateData();
        playerBroadcaster.broadcast(sourceEvent);
        playerBroadcaster.broadcast(new PlayerEvent.Factory().play(podcastPosition));
    }

    public void load() {
        validateData();
        playerBroadcaster.broadcast(sourceEvent);
        playerBroadcaster.broadcast(goToEvent);
    }

    private void validateData() {
        if (sourceEvent == null || goToEvent == null) {
            throw new IllegalAccessError("You must call setData before trying to play or load");
        }
    }
}
