package com.ouchadam.fang.debug;

import android.app.Activity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.parsing.ChannelFinder;
import com.ouchadam.fang.parsing.PodcastParser;
import com.ouchadam.fang.persistance.ChannelPersister;

import java.io.IOException;

public class ParseHelper {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    private final ContentResolver contentResolver;
    private final OnParseFinishedListener listener;

    public interface OnParseFinishedListener {
        void onParseFinished(Channel channel);
    }

    public ParseHelper(ContentResolver contentResolver, OnParseFinishedListener listener) {
        this.contentResolver = contentResolver;
        this.listener = listener;
    }

    public void parseLocal(Activity activity, String fileName) {
        PodcastParser podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance());
        try {
            podcastParser.parse(activity.getAssets().open(fileName));
            new ChannelPersister(contentResolver).persist(podcastParser.getResult(), fileName, 0);
            onCallback(podcastParser.getResult());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse(Context context, String... urls) {
        Intent addServiceIntent = FeedServiceInfo.add(context, urls);
        context.startService(addServiceIntent);
    }

    private synchronized void onCallback(final Channel channel) {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                listener.onParseFinished(channel);
            }
        });
    }

}
