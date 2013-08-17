package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.support.v4.app.LoaderManager;
import com.ouchadam.fang.persistance.DataUpdater;
import com.ouchadam.fang.persistance.Query;
import novoda.android.typewriter.cursor.CursorMarshaller;

public class DataQueryer<T> {

    private DataUpdater<T> dataUpdater;

    private final Context context;
    private final Query query;
    private final CursorMarshaller<T> marshaller;
    private final LoaderManager loaderManager;
    private final DataUpdater.DataUpdatedListener<T> dataUpdatedListener;

    public DataQueryer(Context context, Query query, CursorMarshaller<T> marshaller, LoaderManager loaderManager, DataUpdater.DataUpdatedListener<T> dataUpdatedListener) {
        this.context = context;
        this.query = query;
        this.marshaller = marshaller;
        this.loaderManager = loaderManager;
        this.dataUpdatedListener = dataUpdatedListener;
    }

    public void queryForData() {
        if (dataUpdater != null) {
            dataUpdater.stopWatchingData();
        }
        dataUpdater = new DataUpdater<T>(context, query, marshaller, dataUpdatedListener, loaderManager);
        dataUpdater.startWatchingData();
    }

    public void stop() {
        if (dataUpdater != null) {
            dataUpdater.stopWatchingData();
        }
    }

}
