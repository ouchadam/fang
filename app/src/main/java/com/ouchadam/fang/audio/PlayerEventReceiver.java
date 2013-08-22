package com.ouchadam.fang.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;

import com.ouchadam.fang.presentation.controller.PlayerEvent;
import com.ouchadam.fang.presentation.controller.PlayerEventIntentMarshaller;

class PlayerEventReceiver extends BroadcastReceiver {

    private final PlayerEventCallbacks callbacks;

    PlayerEventReceiver(PlayerEventCallbacks callbacks) {
        this.callbacks = callbacks;
    }

    public void register(Context context) {
        context.registerReceiver(this, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        for (PlayerEvent.Event event : PlayerEvent.Event.values()) {
            intentFilter.addAction(event.toAction());
        }
        return intentFilter;
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    interface PlayerEventCallbacks {
        void onPlay(PodcastPosition position);
        void onPause();
        void onStop();
        void onNewSource(long itemId, Uri source);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerEvent from = new PlayerEventIntentMarshaller().from(intent);

        switch (from.getEvent()) {
            case PLAY:
                callbacks.onPlay(from.getPosition());
                break;
            case PAUSE:
                callbacks.onPause();
                break;
            case STOP:
                callbacks.onStop();
                break;
            case NEW_SOURCE:
                callbacks.onNewSource(from.getId(), from.getSource());
                break;
            default:
                throw new RuntimeException("recieved an unhandled event : " + from.getEvent());
        }

    }
}
