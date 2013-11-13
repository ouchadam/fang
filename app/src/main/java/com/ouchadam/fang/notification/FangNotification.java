package com.ouchadam.fang.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.audio.event.PlayerEvent;

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

    public void show(Bitmap image, FullItem fullItem, boolean playing) {
        showNotification(image, fullItem, playing);
    }

    private void showNotification(Bitmap channelImage, FullItem fullItem, boolean playing) {
        NotificationCompat.Builder normal = createNormal(channelImage, fullItem);

        RemoteViews customNotifView = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        customNotifView.setTextViewText(R.id.title, fullItem.getItem().getTitle());
        customNotifView.setTextViewText(R.id.channel, fullItem.getChannelTitle());
        customNotifView.setImageViewBitmap(R.id.notification_big_channel_image, channelImage);

        PendingIntent closeIntent = new RemoteClick().createPendingIntent(context, new PlayerEvent.Factory().stop());
        PendingIntent pauseIntent = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().pause(), fullItem.getItemId());
        PendingIntent playIntent = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().play(), fullItem.getItemId());

        customNotifView.setOnClickPendingIntent(R.id.notification_close, closeIntent);
        customNotifView.setOnClickPendingIntent(R.id.play, playIntent);
        customNotifView.setOnClickPendingIntent(R.id.pause, pauseIntent);

        customNotifView.setViewVisibility(R.id.play, playing ? View.GONE : View.VISIBLE);
        customNotifView.setViewVisibility(R.id.pause, playing ? View.VISIBLE : View.GONE);

        Notification notification = normal.build();
        notification.bigContentView = customNotifView;

        notificationManager.notify(ID, notification);
    }

    private static class RemoteClick {

        public PendingIntent createMediaPendingIntent(Context context, PlayerEvent event, long itemId) {
            Intent intent = createIntent(event);
//            intent.setAction(event.getEvent().toNotification());
            intent.putExtra("itemId", itemId);
            return addClickListener(context, intent);
        }

        public PendingIntent createPendingIntent(Context context, PlayerEvent event) {
            Intent intent = createIntent(event);
            return addClickListener(context, intent);
        }

        private Intent createIntent(PlayerEvent event) {
            return new Intent(event.getEvent().toAction(PlayerEvent.Event.ACTION_PREFIX));
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
