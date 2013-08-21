package com.ouchadam.fang.audio;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

public class AudioServiceBinder {

    private final Context context;
    private Connection connection;

    public interface OnStateSync {
        void onSync(boolean playing, PodcastPosition position);
    }

    public AudioServiceBinder(Context context) {
        this.context = context;
    }

    public void bindService(OnStateSync onBindStateSyncListener) {
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

        private final OnStateSync listener;
        private AudioService audioService;

        private Connection(OnStateSync listener) {
            this.listener = listener;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            audioService = ((AudioService.LocalBinder) binder).getService();
            audioService.setSyncListener(listener);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            audioService = null;
        }

    }

}
