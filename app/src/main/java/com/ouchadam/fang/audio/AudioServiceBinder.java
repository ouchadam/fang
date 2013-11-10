package com.ouchadam.fang.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

public class AudioServiceBinder {

    private final Context context;
    private OnStateSync onBindStateSyncListener;
    private Connection connection;

    public interface OnStateSync {

        void onSync(SyncEvent syncEvent);
    }
    public AudioServiceBinder(Context context, OnStateSync onBindStateSyncListener) {
        this.context = context;
        this.onBindStateSyncListener = onBindStateSyncListener;
    }

    public void bindService() {
        if (connection == null) {
            connection = new Connection(onBindStateSyncListener);
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

        private final OnStateSync listener;
        private AudioService audioService;

        private Connection(OnStateSync listener) {
            this.listener = listener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            Log.e("???", "onServiceConnected");
            audioService = ((AudioService.LocalBinder) binder).getService();
            audioService.setSyncListener(listener);
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
