package com.ouchadam.fang.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.util.Log;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PlayerEventIntentMarshaller;

public class PlayerEventReceiver extends BroadcastReceiver {

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
            intentFilter.addAction(event.toAction(PlayerEvent.Event.ACTION_PREFIX));
        }
        return intentFilter;
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    interface PlayerEventCallbacks {
        void onPlay();

        void onPlay(PodcastPosition position);

        void onPause();

        void onPlayPause();

        void onStop();

        void onNewSource(int playlistPosition, String playlist);

        void gotoPosition(PodcastPosition position);

        void onReset();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerEvent from = new PlayerEventIntentMarshaller(PlayerEvent.Event.ACTION_PREFIX).from(intent);
        Log.e("!!!", "onReceive : " + from.getEvent().name() + " : id : " + from.getId());

        switch (from.getEvent()) {
            case PLAY:
                handlePlay(from);
                break;
            case PAUSE:
                callbacks.onPause();
                break;
            case PLAY_PAUSE:
                callbacks.onPlayPause();
                break;
            case STOP:
                callbacks.onStop();
                break;
            case NEW_SOURCE:
                callbacks.onNewSource(from.getPlaylistPosition(), "TODO");
                break;
            case GOTO:
                callbacks.gotoPosition(from.getPosition());
                break;
            case RESET:
                callbacks.onReset();
                break;
            default:
                throw new RuntimeException("received an unhandled event : " + from.getEvent());
        }

    }

    private void handlePlay(PlayerEvent from) {
        PodcastPosition position = from.getPosition();
        if (position != null) {
            callbacks.onPlay(position);
        } else {
            callbacks.onPlay();
        }
    }
}
