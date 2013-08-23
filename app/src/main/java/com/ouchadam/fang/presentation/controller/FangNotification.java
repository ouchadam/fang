package com.ouchadam.fang.presentation.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;

public class FangNotification {

    private static final int ID = 0xA3;
    private final NotificationManager notificationManager;
    private final Context context;

    public static FangNotification from(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        return new FangNotification(notificationManager, context);
    }

    private FangNotification(NotificationManager notificationManager, Context context) {
        this.notificationManager = notificationManager;
        this.context = context;
    }

    public void show(FullItem fullItem) {
        showNotification(fullItem);
    }

    private void showNotification(FullItem fullItem) {
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentTitle(fullItem.getItem().getTitle());
        builder.setContentText("This is my content");
        builder.setSmallIcon(R.drawable.play_button);

        notificationManager.notify(ID, builder.build());
    }

    public void dismiss() {
        hideNotification();
    }

    private void hideNotification() {
        notificationManager.cancel(ID);
    }

}
