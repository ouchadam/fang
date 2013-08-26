package com.ouchadam.fang.notification;

import android.content.Context;
import android.content.Intent;

import com.ouchadam.fang.FangBroadcaster;
import com.ouchadam.fang.notification.PlayerEventNotificationIntentMarshaller;
import com.ouchadam.fang.presentation.controller.PlayerEvent;

public class PodcastPlayerNotificationEventBroadcaster extends FangBroadcaster<PlayerEvent> {

    private final long itemId;

    public PodcastPlayerNotificationEventBroadcaster(long itemId, Context context) {
        super(context);
        this.itemId = itemId;
    }

    @Override
    protected Intent marshall(PlayerEvent what) {
        return new PlayerEventNotificationIntentMarshaller().to(itemId, what);
    }

}
