package com.ouchadam.fang.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class AudioServiceBinder {

    private final Context context;
    private final OnStateSync onBindStateSyncListener;
    private final CompletionListener completionListener;

    private Connection connection;

    public AudioServiceBinder(Context context, OnStateSync onBindStateSyncListener, CompletionListener completionListener) {
        this.context = context;
        this.onBindStateSyncListener = onBindStateSyncListener;
        this.completionListener = completionListener;
    }

    public void bindService() {
        if (connection == null) {
            connection = new Connection(onBindStateSyncListener, completionListener);
        }
        Intent intent = new Intent(context, AudioService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (connectionIsAvailable()) {
            connection.fangUnbind();
            context.unbindService(connection);
            connection = null;
        }
    }

    private boolean connectionIsAvailable() {
        return connection != null;
    }

    private static class Connection implements ServiceConnection {

        private final OnStateSync syncListener;
        private CompletionListener completionListener;
        private AudioService audioService;

        private Connection(OnStateSync syncListener, CompletionListener completionListener) {
            this.syncListener = syncListener;
            this.completionListener = completionListener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.LocalBinder) binder).getService();
            audioService.setSyncListener(syncListener);
            audioService.setCompletionListener(completionListener);
            audioService.fangBind();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }

        public void fangUnbind() {
            if (audioService != null) {
                audioService.fangUnbind();
            } else {
                Log.e("!!!!", "Tried to unbind but service has already disconnected");
            }
        }
    }

}
