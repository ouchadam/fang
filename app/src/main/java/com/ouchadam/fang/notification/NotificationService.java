package com.ouchadam.fang.notification;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.util.Log;

import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class NotificationService extends IntentService {

    public static final String EXTRA_IS_PLAYING = "isPlaying";
    public static final String EXTRA_ITEM_ID = "test";

    private FangNotification fangNotification;

    public static void start(Context context, long itemId, boolean isPlaying) {
        Intent intent = new Intent(context, NotificationService.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_IS_PLAYING, isPlaying);
        context.startService(intent);
    }

    public NotificationService() {
        super(NotificationService.class.getSimpleName());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        fangNotification = FangNotification.from(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (isValid(intent)) {
            long itemId = intent.getLongExtra(EXTRA_ITEM_ID, -1L);
            boolean isPlaying = intent.getBooleanExtra(EXTRA_IS_PLAYING, true);
            Log.e("!!!!", "Acting on event : " + itemId + " with isPlaying? : " + isPlaying);

            FullItem item = getFullItem(itemId);
            if (item != null) {
                try {
                    Bitmap channelImage = getChannelImage(item);
                    fangNotification.show(channelImage, item, isPlaying);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private Bitmap getChannelImage(FullItem item) throws IOException {
        int imageWidth = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_width);
        int imageHeight = getResources().getDimensionPixelSize(android.R.dimen.notification_large_icon_height);
        return Picasso.with(this).load(item.getImageUrl()).resize(imageWidth, imageHeight).get();
    }

    private FullItem getFullItem(long itemId) {
        Query query = getQueryValues(itemId);
        Cursor cursor = getContentResolver().query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder);

        FullItem item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new FullItemMarshaller().marshall(cursor);
        }
        if (cursor != null) {
            cursor.close();
        }
        return item;
    }

    private boolean isValid(Intent intent) {
        return intent != null && intent.hasExtra(EXTRA_ITEM_ID) && intent.getLongExtra(EXTRA_ITEM_ID, -1L) != -1L;
    }

    private Query getQueryValues(long itemId) {
        return new Query.Builder().
                withUri(FangProvider.getUri(Uris.FULL_ITEM)).
                withSelection(Tables.Item._id.name() + "=?").
                withSelectionArgs(new String[]{String.valueOf(itemId)}).build();
    }

}
