package com.ouchadam.fang.audio.event;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.presentation.IntentMarshaller;

import java.io.Serializable;

public class PlayerEventIntentMarshaller implements IntentMarshaller<PlayerEvent> {

    private static final String POSITION = "position";
    private static final String playListPosition = "playlistPosition";

    private final String actionPrefix;

    public PlayerEventIntentMarshaller(String actionPrefix) {
        this.actionPrefix = actionPrefix;
    }

    @Override
    public Intent to(long itemId, PlayerEvent what) {
        Intent intent = new Intent(what.getEvent().toAction(actionPrefix));
        intent.putExtra(playListPosition, what.getPlaylistPosition());
        putExtraIfAvailable(intent, POSITION, what.getPosition());
        return intent;
    }

    private void putExtraIfAvailable(Intent intent, String key, Serializable serializable) {
        if (serializable != null) {
            intent.putExtra(key, serializable);
        }
    }

    @Override
    public PlayerEvent from(Intent intent) {
        PlayerEvent.Event event = PlayerEvent.Event.fromAction(actionPrefix, intent.getAction());
        PlayerEvent.Factory factory = new PlayerEvent.Factory();
        switch (event) {
            case PLAY:
                return factory.play((PodcastPosition) intent.getSerializableExtra(POSITION));
            case NEW_SOURCE:
                int playlistPosition = intent.getIntExtra(playListPosition, 0);
                return factory.newSource(playlistPosition, "PLAYLIST");
            case PAUSE:
                return factory.pause();
            case PLAY_PAUSE:
                return factory.playPause();
            case STOP:
                return factory.stop();
            case REWIND:
                return factory.rewind();
            case FAST_FORWARD:
                return factory.fastForward();
            case GOTO:
                return factory.goTo((PodcastPosition) intent.getSerializableExtra(POSITION));
            case RESET:
                return factory.reset();
            default:
                throw new RuntimeException("Intent has unhandled event : " + event);
        }
    }
}
