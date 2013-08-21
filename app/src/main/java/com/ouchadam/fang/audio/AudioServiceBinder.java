package com.ouchadam.fang.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class AudioServiceBinder {

    private final Context context;
    private Connection connection;

    public interface OnBindStateSync {
        void onBind(boolean isPlaying);
    }

    public AudioServiceBinder(Context context) {
        this.context = context;
    }

    public void bindService(OnBindStateSync onBindStateSyncListener) {
        if (connection == null) {
            connection = new Connection(onBindStateSyncListener);
        }
        Intent intent = new Intent(context, AudioService.class);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    public void unbind() {
        if (connectionIsAvailable()) {
            context.unbindService(connection);
            connection = null;
        }
    }

    private boolean connectionIsAvailable() {
        return connection != null;
    }

    private static class Connection implements ServiceConnection {

        private final OnBindStateSync listener;
        private AudioService audioService;

        private Connection(OnBindStateSync listener) {
            this.listener = listener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.LocalBinder) binder).getService();
            listener.onBind(audioService.isPlaying());
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }

    }

}
