package com.ouchadam.fang.notification;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ouchadam.fang.audio.event.PlayerEvent;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        PlayerEvent event = new PlayerEventNotificationIntentMarshaller().from(intent);
        boolean isPlaying = event.getEvent() == PlayerEvent.Event.PLAY;
        Log.e("!!!!" , "Received event : " + event.getEvent().name() + " to notification with ID : " + event.getId());
        NotificationService.start(context, event.getId(), isPlaying);
//        new PodcastPlayerEventBroadcaster(PlayerEvent.Event.ACTION_PREFIX, context).broadcast(event);
    }

}
