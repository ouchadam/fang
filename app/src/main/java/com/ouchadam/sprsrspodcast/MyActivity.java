package com.ouchadam.sprsrspodcast;

import android.app.Activity;
import android.os.Bundle;

import com.novoda.sexp.parser.ParseFinishWatcher;
import com.ouchadam.sprsrspodcast.domain.channel.Channel;
import com.ouchadam.sprsrspodcast.parsing.ChannelFinder;
import com.ouchadam.sprsrspodcast.parsing.PodcastParser;
import com.ouchadam.sprsrspodcast.persistance.ContentProviderOperationExecutable;
import com.ouchadam.sprsrspodcast.persistance.OperationWrapperImpl;
import com.ouchadam.sprsrspodcast.persistance.Persister;
import com.ouchadam.sprsrspodcast.persistance.database.Executable;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.BaseMarshaller;
import com.ouchadam.sprsrspodcast.persistance.database.marshaller.ChannelMarshaller;

import java.io.IOException;

public class MyActivity extends Activity implements ParseFinishWatcher {

    private PodcastParser podcastParser;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        podcastParser = PodcastParser.newInstance(ChannelFinder.newInstance(), this);
        try {
            podcastParser.parse(getAssets().open(""));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinish() {
        testPersist(podcastParser.getResult());
    }

    private void testPersist(Channel channel) {
        try {
            new Persister<Channel>(getExecutor(), getChannelMarshaller()).persist(channel);
        } catch (Executable.ExecutionFailure executionFailure) {
            executionFailure.printStackTrace();
        }
    }

    private BaseMarshaller<Channel> getChannelMarshaller() {
        return new ChannelMarshaller(new OperationWrapperImpl());
    }

    private ContentProviderOperationExecutable getExecutor() {
        return new ContentProviderOperationExecutable(getContentResolver());
    }
}
