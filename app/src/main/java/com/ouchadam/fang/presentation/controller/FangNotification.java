package com.ouchadam.fang.presentation.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

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

    public void show(Bitmap image, FullItem fullItem) {
        showNotification(image, fullItem);
    }

    private void showNotification(Bitmap channelImage, FullItem fullItem) {
        NotificationCompat.Builder normal = createNormal(channelImage, fullItem);

        RemoteViews customNotifView = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        customNotifView.setTextViewText(R.id.title, fullItem.getItem().getTitle());
        customNotifView.setTextViewText(R.id.channel, fullItem.getChannelTitle());
        customNotifView.setImageViewBitmap(R.id.notification_big_channel_image, channelImage);

        Notification notification = normal.build();
        notification.bigContentView = customNotifView;

        notificationManager.notify(ID, notification);
    }

    private NotificationCompat.Builder createNormal(Bitmap channelImage, FullItem fullItem) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        builder.setContentTitle(fullItem.getItem().getTitle());
        builder.setContentText("This is my content");
        builder.setSmallIcon(R.drawable.play_button);

        builder.setLargeIcon(channelImage);

        builder.setAutoCancel(false);
        builder.setOngoing(true);

        builder.setPriority(Notification.PRIORITY_HIGH);
        return builder;
    }

    public void dismiss() {
        hideNotification();
    }

    private void hideNotification() {
        notificationManager.cancel(ID);
    }

}
