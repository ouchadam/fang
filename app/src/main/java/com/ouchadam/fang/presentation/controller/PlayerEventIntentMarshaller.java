package com.ouchadam.fang.presentation.controller;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.ouchadam.fang.audio.PodcastPosition;

import java.io.Serializable;

public class PlayerEventIntentMarshaller implements IntentMarshaller<PlayerEvent> {

    private static final String SOURCE = "source";
    private static final String POSITION = "position";

    @Override
    public Intent to(PlayerEvent what) {
        Intent intent = new Intent(what.getEvent().toAction());
        putExtraIfAvailable(intent, SOURCE, what.getSource());
        putExtraIfAvailable(intent, POSITION, what.getPosition());
        return intent;
    }

    private void putExtraIfAvailable(Intent intent, String key, Serializable serializable) {
        if (serializable != null) {
            intent.putExtra(key, serializable);
        }
    }

    private void putExtraIfAvailable(Intent intent, String key, Parcelable parcelable) {
        if (parcelable != null) {
            intent.putExtra(key, parcelable);
        }
    }

    @Override
    public PlayerEvent from(Intent intent) {
        PlayerEvent.Event event = PlayerEvent.Event.fromAction(intent.getAction());
        PlayerEvent.Factory factory = new PlayerEvent.Factory();
        switch (event) {
            case PLAY:
                return factory.play((PodcastPosition) intent.getSerializableExtra(POSITION));
            case NEW_SOURCE:
                return factory.newSource((Uri) intent.getParcelableExtra(SOURCE));
            case PAUSE:
                return factory.pause();
            default:
                throw new RuntimeException("Intent has unhandled event : " + event);
        }
    }
}
