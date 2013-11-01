package com.ouchadam.fang.notification;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.IntentMarshaller;
import com.ouchadam.fang.presentation.PlayerEvent;

import java.io.Serializable;

public class PlayerEventNotificationIntentMarshaller implements IntentMarshaller<PlayerEvent> {

    private static final String SOURCE = "source";
    private static final String POSITION = "position";
    private static final String ID = "id";

    @Override
    public Intent to(long itemId, PlayerEvent what) {
        Intent intent = new Intent(what.getEvent().toAction(PlayerEvent.Event.NOTIFICATION_PREFIX));
        putExtraIfAvailable(intent, SOURCE, what.getSource());
        intent.putExtra(ID, itemId);
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
        PlayerEvent.Event event = PlayerEvent.Event.fromAction(PlayerEvent.Event.NOTIFICATION_PREFIX, intent.getAction());
        long itemId = intent.getLongExtra(ID, -1L);

        PlayerEvent.Factory factory = new PlayerEvent.Factory();
        PodcastPosition position = (PodcastPosition) intent.getSerializableExtra(POSITION);
        switch (event) {
            case PLAY:
                return new PlayerEvent.Builder(factory.play(position)).withId(itemId).build();
            case NEW_SOURCE:
                return factory.newSource(itemId, (Uri) intent.getParcelableExtra(SOURCE));
            case PAUSE:
                return new PlayerEvent.Builder(factory.pause()).withId(itemId).build();
            case STOP:
                return factory.stop();
            case GOTO:
                return factory.goTo(position);
            default:
                throw new RuntimeException("Intent has unhandled event : " + event);
        }
    }
}
