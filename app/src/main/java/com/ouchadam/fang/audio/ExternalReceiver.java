package com.ouchadam.fang.audio;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.telephony.TelephonyManager;

import com.ouchadam.fang.presentation.PlayerEvent;
import com.ouchadam.fang.presentation.PodcastPlayerEventBroadcaster;

class ExternalReceiver extends BroadcastReceiver {

    public void register(Context context) {
        context.registerReceiver(this, getIntentFilter());
    }

    private IntentFilter getIntentFilter() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(AudioManager.ACTION_AUDIO_BECOMING_NOISY);
        intentFilter.addAction(TelephonyManager.ACTION_PHONE_STATE_CHANGED);
        return intentFilter;
    }

    public void unregister(Context context) {
        context.unregisterReceiver(this);
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (AudioManager.ACTION_AUDIO_BECOMING_NOISY.equals(intent.getAction())) {
            new PodcastPlayerEventBroadcaster(context).broadcast(new PlayerEvent.Factory().pause());
        } else if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(intent.getAction())) {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            if (state != null) {
                if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
                    new PodcastPlayerEventBroadcaster(context).broadcast(new PlayerEvent.Factory().pause());
                }
            }
        }

    }
}
