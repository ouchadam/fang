package com.ouchadam.fang.audio;

import android.util.Log;

import com.novoda.notils.java.Collections;
import com.novoda.notils.logger.Novogger;
import com.ouchadam.fang.audio.event.PlayerEvent;

import java.util.List;

class EventQueue {

    private final List<PlayerEvent> events;

    public interface OnEvent {
        void onEvent(PlayerEvent playerEvent);
    }

    public EventQueue() {
        this.events = Collections.newArrayList();
    }

    public void add(PlayerEvent playerEvent) {
        Log.e("XXX", "Queuing : " + playerEvent.getEvent().name());
        this.events.add(playerEvent);
    }

    public void dequeue(OnEvent forEach) {
        for (PlayerEvent event : events) {
            forEach.onEvent(event);
        }
        events.clear();
    }

    public synchronized void clear() {
        events.clear();
    }

}
