package com.ouchadam.fang;

import android.content.Context;
import android.content.Intent;

public abstract class FangBroadcaster<T> implements Broadcaster<T> {

    private final Context context;

    public FangBroadcaster(Context context) {
        this.context = context;
    }

    @Override
    public final void broadcast(T what) {
        broadcast(marshall(what));
    }

    protected abstract Intent marshall(T what);

    private void broadcast(Intent intent) {
        context.sendBroadcast(intent);
    }
}
