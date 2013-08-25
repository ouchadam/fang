package com.ouchadam.fang.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ouchadam.fang.presentation.controller.PlayerEvent;
import com.ouchadam.fang.presentation.controller.PlayerEventIntentMarshaller;
import com.ouchadam.fang.presentation.controller.PodcastPlayerEventBroadcaster;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerEvent event = new PlayerEventIntentMarshaller(PlayerEvent.Event.NOTIFICATION_PREFIX).from(intent);
        boolean isPlaying = event.getEvent() == PlayerEvent.Event.PLAY;
        long itemId = intent.getLongExtra("itemId", -1L);
        NotificationService.start(context, itemId, isPlaying);
        new PodcastPlayerEventBroadcaster(context).broadcast(event);
    }

}
