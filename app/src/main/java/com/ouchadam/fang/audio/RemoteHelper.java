package com.ouchadam.fang.audio;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.RemoteControlClient;
import android.util.Log;
import android.view.KeyEvent;

import com.ouchadam.fang.audio.event.PlayerEvent;
import com.ouchadam.fang.audio.event.PodcastPlayerEventBroadcaster;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class RemoteHelper {

    private final Context context;
    private final AudioManager audioManager;
    private RemoteControlClient remoteControlClient;
    private ComponentName eventReceiver;

    public RemoteHelper(Context context) {
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        this.context = context;
    }

    public void init() {
        eventReceiver = new ComponentName(context, MusicIntentReceiver.class);
        audioManager.registerMediaButtonEventReceiver(eventReceiver);

        Intent intent = new Intent(Intent.ACTION_MEDIA_BUTTON);
        intent.setComponent(eventReceiver);
        remoteControlClient = new RemoteControlClient(PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        audioManager.registerRemoteControlClient(remoteControlClient);

        remoteControlClient.setTransportControlFlags(
                RemoteControlClient.FLAG_KEY_MEDIA_PLAY_PAUSE | RemoteControlClient.FLAG_KEY_MEDIA_NEXT |
                        RemoteControlClient.FLAG_KEY_MEDIA_PREVIOUS);
    }

    public void update(Playlist.PlaylistItem playlistItem) {
        update(playlistItem.imageUrl, playlistItem.title, playlistItem.channel);
    }

    public void update(final String imgUrl, final String title, final String channel) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Picasso.with(context).load(imgUrl).get();
                    remoteControlClient.editMetadata(true).putBitmap(RemoteControlClient.MetadataEditor.BITMAP_KEY_ARTWORK, bitmap).putString(MediaMetadataRetriever.METADATA_KEY_TITLE, title)
                            .putString(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST, channel).apply();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void setPlaying() {
        remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PLAYING);
    }

    public void setPaused() {
        remoteControlClient.setPlaybackState(RemoteControlClient.PLAYSTATE_PAUSED);
    }

    public void unregister() {
        audioManager.unregisterMediaButtonEventReceiver(eventReceiver);
        audioManager.unregisterRemoteControlClient(remoteControlClient);
    }

    public static class MusicIntentReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            PodcastPlayerEventBroadcaster eventBroadcaster = new PodcastPlayerEventBroadcaster(context);

            if (intent.getAction().equals(Intent.ACTION_MEDIA_BUTTON)) {
                KeyEvent keyEvent = (KeyEvent) intent.getExtras().get(Intent.EXTRA_KEY_EVENT);

                if (keyEvent.getAction() != KeyEvent.ACTION_DOWN)
                    return;

                switch (keyEvent.getKeyCode()) {
                    case KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE:
                        eventBroadcaster.broadcast(new PlayerEvent.Factory().playPause());
                        break;
                    case KeyEvent.KEYCODE_MEDIA_NEXT:
                        break;
                    case KeyEvent.KEYCODE_MEDIA_PREVIOUS:
                        // TODO: ensure that doing this in rapid succession actually plays the previous song
                        break;
                }
            }
        }
    }

}
