package com.ouchadam.fang.debug;

import android.app.Activity;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.parsing.ChannelFinder;
import com.ouchadam.fang.parsing.PodcastParser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class ParseHelper implements ParseFinishWatcher {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());
    private final OnParseFinishedListener listener;
    private PodcastParser podcastParser;

    public interface OnParseFinishedListener {
        void onParseFinished(Channel channel);
    }

    public ParseHelper(OnParseFinishedListener listener) {
        this.listener = listener;
    }

    public void parse(Activity activity, String fileName) {
        podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), this);
        try {
            podcastParser.parse(activity.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void parse(final String... urls) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String url : urls) {
                    podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), ParseHelper.this);
                    podcastParser.parse(getInputStream(url));
                }
            }
        }).start();
    }

    private InputStream getInputStream(String url) {
        URL oracle = null;
        Log.e("!!!", "Fetching stream for : " + url);
        try {
            oracle = new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        InputStream in = null;
        try {
            return oracle.openStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void onFinish() {
        HANDLER.post(new Runnable() {
            @Override
            public void run() {
                listener.onParseFinished(podcastParser.getResult());
            }
        });
    }
}
