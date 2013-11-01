package com.ouchadam.fang;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.DataUpdater;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.item.DataQueryer;
import novoda.android.typewriter.cursor.CursorMarshaller;

import java.util.List;

public class ItemQueryer implements DataUpdater.DataUpdatedListener<FullItem> {

    private final long itemId;
    private final OnItemListener onItemListener;
    private final DataQueryer<FullItem> itemQueryer;

    public interface OnItemListener {
        void onItem(FullItem item);
    }

    public ItemQueryer(Context context, long itemId, LoaderManager loaderManager, OnItemListener onItemListener) {
        this.itemId = itemId;
        this.onItemListener = onItemListener;
        this.itemQueryer = new DataQueryer<FullItem>(context, getQueryValues(), getMarshaller(), loaderManager, this);
    }

    public void query() {
        itemQueryer.queryForData();
    }

    private CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    public Query getQueryValues() {
        return new Query.Builder().
                withUri(FangProvider.getUri(Uris.FULL_ITEM)).
                withSelection(Tables.Item._id.name() + "=?").
                withSelectionArgs(new String[]{String.valueOf(itemId)}).build();
    }

    @Override
    public void onDataUpdated(List<FullItem> data) {
        if (!data.isEmpty() && data.size() == 1) {
            onItemListener.onItem(data.get(0));
        }
    }

    public void stop() {
        itemQueryer.stop();
    }

}
