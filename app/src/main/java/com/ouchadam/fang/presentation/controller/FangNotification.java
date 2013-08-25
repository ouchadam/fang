package com.ouchadam.fang.presentation.controller;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.PlayerEventReceiver;
import com.ouchadam.fang.domain.FullItem;

import java.util.Random;

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

        PendingIntent pendingIntent = new RemoteClick().createPendingIntent(context, new PlayerEvent.Factory().stop(), fullItem.getItemId());

        customNotifView.setOnClickPendingIntent(R.id.notification_close, pendingIntent);

        Notification notification = normal.build();
        notification.bigContentView = customNotifView;

        notificationManager.notify(ID, notification);
    }

    private static class RemoteClick {

        private static final int UNNEEDED_REQUEST_CODE = 0;

        public PendingIntent createPendingIntent(Context context, PlayerEvent event, long itemId) {
            Intent intent = createIntent(event, itemId);
            return addClickListener(context, intent);
        }

        private Intent createIntent(PlayerEvent event, long itemId) {
//            Intent intent = new PlayerEventIntentMarshaller().to(itemId, event);
            Intent intent = new Intent(event.getEvent().toAction());
            return intent;
        }

        private PendingIntent addClickListener(Context context, Intent intent) {
            return PendingIntent.getBroadcast(context, (int) System.currentTimeMillis(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
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
