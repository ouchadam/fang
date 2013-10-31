package com.ouchadam.fang.debug;

import android.app.Activity;

import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.parsing.ChannelFinder;
import com.ouchadam.fang.parsing.PodcastParser;
import com.ouchadam.fang.persistance.ChannelPersister;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

public class ParseHelper {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    private final ContentResolver contentResolver;
    private final OnParseFinishedListener listener;

    private PodcastParser podcastParser;

    public interface OnParseFinishedListener {
        void onParseFinished(Channel channel);
    }

    public ParseHelper(ContentResolver contentResolver, OnParseFinishedListener listener) {
        this.contentResolver = contentResolver;
        this.listener = listener;
    }

    public void parse(Activity activity, String fileName) {
        podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), parseFinishWatcher);
        try {
            podcastParser.parse(activity.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private final ParseFinishWatcher parseFinishWatcher = new ParseFinishWatcher() {
        @Override
        public void onFinish() {
            new ChannelPersister(contentResolver).persist(podcastParser.getResult(), "www.dummyaddress.com");
            onCallback(podcastParser.getResult());
        }
    };

    public void parse(Context context, String... urls) {
        Intent addServiceIntent = ServiceInfo.add(context, urls);
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
