package com.ouchadam.fang.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.RemoteViews;

import com.ouchadam.fang.R;
import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.presentation.controller.FragmentControllerActivity;

import java.util.Random;

public class FangNotification {

    public static final int ID = 0xA3;
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

    public Notification get() {
        return createBase().build();
    }

    public void show(Bitmap image, FullItem fullItem, boolean playing) {
        showNotification(image, fullItem, playing);
    }

    private void showNotification(Bitmap channelImage, FullItem fullItem, boolean playing) {
        NotificationCompat.Builder normal = createNormal(channelImage, fullItem);
        Notification notification = normal.build();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            notification.bigContentView = createBigContentNotification(channelImage, fullItem, playing);
        }
        notificationManager.notify(ID, notification);
    }

    private RemoteViews createBigContentNotification(Bitmap channelImage, FullItem fullItem, boolean playing) {
        RemoteViews customNotifView = new RemoteViews(context.getPackageName(), R.layout.notification_big);
        customNotifView.setTextViewText(R.id.title, fullItem.getItem().getTitle());
        customNotifView.setTextViewText(R.id.channel, fullItem.getChannelTitle());
        customNotifView.setImageViewBitmap(R.id.notification_big_channel_image, channelImage);

        PendingIntent closeIntent = new RemoteClick().createPendingIntent(context, new PlayerEvent.Factory().stop());
        PendingIntent pauseIntent = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().playPause(), fullItem.getItemId());
        PendingIntent playIntent = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().playPause(), fullItem.getItemId());
        PendingIntent rewind = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().rewind(), fullItem.getItemId());
        PendingIntent fastForward = new RemoteClick().createMediaPendingIntent(context, new PlayerEvent.Factory().fastForward(), fullItem.getItemId());

        customNotifView.setOnClickPendingIntent(R.id.notification_close, closeIntent);
        customNotifView.setOnClickPendingIntent(R.id.play, playIntent);
        customNotifView.setOnClickPendingIntent(R.id.pause, pauseIntent);
        customNotifView.setOnClickPendingIntent(R.id.rewind, rewind);
        customNotifView.setOnClickPendingIntent(R.id.fast_forward, fastForward);

        customNotifView.setViewVisibility(R.id.play, playing ? View.GONE : View.VISIBLE);
        customNotifView.setViewVisibility(R.id.pause, playing ? View.VISIBLE : View.GONE);
        return customNotifView;
    }

    private static class RemoteClick {

        public PendingIntent createMediaPendingIntent(Context context, PlayerEvent event, long itemId) {
            Intent intent = createIntent(event);
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
            return PendingIntent.getBroadcast(context, (int) System.currentTimeMillis() + new Random().nextInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
    }

    private NotificationCompat.Builder createNormal(Bitmap channelImage, FullItem fullItem) {
        NotificationCompat.Builder builder = createBase();
        builder.setContentTitle(fullItem.getItem().getTitle());
        builder.setContentText(fullItem.getChannelTitle());
        builder.setLargeIcon(channelImage);
        return builder;
    }

    private NotificationCompat.Builder createBase() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setSmallIcon(R.drawable.play_button);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, FragmentControllerActivity.class), PendingIntent.FLAG_CANCEL_CURRENT));

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
