package com.ouchadam.fang.audio;

import android.app.IntentService;
import android.content.Intent;
import android.database.Cursor;

import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.controller.FangNotification;
import com.ouchadam.fang.presentation.controller.FullItemMarshaller;
import com.ouchadam.fang.presentation.controller.ItemMarshaller;
import com.ouchadam.fang.presentation.controller.ItemQueryer;

public class NotificationService extends IntentService {


    private FangNotification fangNotification;

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
            long itemId = intent.getLongExtra("test", -1L);
            FullItem item = getFullItem(itemId);
            if (item != null) {
                fangNotification.show(item);
            }
        }
    }

    private FullItem getFullItem(long itemId) {
        Query query = getQueryValues(itemId);
        Cursor cursor = getContentResolver().query(query.uri, query.projection, query.selection, query.selectionArgs, query.sortOrder);

        FullItem item = null;
        if (cursor != null && cursor.moveToFirst()) {
            item = new FullItemMarshaller().marshall(cursor);
        }
        return item;
    }

    private boolean isValid(Intent intent) {
        return intent != null && intent.hasExtra("test") && intent.getLongExtra("test", -1L) != -1L;
    }

    private Query getQueryValues(long itemId) {
        return new Query.Builder().
                withUri(FangProvider.getUri(Uris.FULL_ITEM)).
                withSelection(Tables.Item._id.name() + "=?").
                withSelectionArgs(new String[]{String.valueOf(itemId)}).build();
    }

}
