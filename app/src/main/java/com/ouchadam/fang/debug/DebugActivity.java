package com.ouchadam.fang.debug;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.preference.Preference;
import android.widget.Toast;
import com.ouchadam.fang.R;
import com.ouchadam.fang.api.search.ItunesSearch;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.api.search.SearchResult;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.persistance.*;

public class DebugActivity extends BasePreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromXml(R.xml.debug_layout);
        setPreferenceListener("persist_local_data", persistData);
        setPreferenceListener("persist_live_data", fetchLiveAndPersistData);
        setPreferenceListener("delete_tables", deleteData);
        setPreferenceListener("add_to_playlist", addToPlaylist);
        setPreferenceListener("search_itunes", searchItunes);
    }

    private final Preference.OnPreferenceClickListener persistData = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            new DatabaseCleaner(new ContentProviderOperationExecutable(getContentResolver())).deleteTestData();
            ParseHelper parseHelper = new ParseHelper(getContentResolver(), onParseFinishedListener);
            parseHelper.parseLocal(DebugActivity.this, "feed_cnet_small.xml");
            parseHelper.parseLocal(DebugActivity.this, "feed_hsw_small.xml");
            return false;
        }
    };


    private final Preference.OnPreferenceClickListener fetchLiveAndPersistData = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            new DatabaseCleaner(new ContentProviderOperationExecutable(getContentResolver())).deleteTestData();
            ParseHelper parseHelper = new ParseHelper(getContentResolver(), onParseFinishedListener);
            parseHelper.parse(DebugActivity.this,
                    "http://www.cnet.co.uk/feeds/podcasts/",
                    "http://www.howstuffworks.com/podcasts/stuff-you-should-know.rss",
                    "http://www.howstuffworks.com/podcasts/stuff-to-blow-your-mind.rss",
                    "http://feeds.99percentinvisible.org/99percentinvisible",
                    "http://www.giantbomb.com/podcast-xml/giant-bombcast/",
                    "http://feeds.feedburner.com/AndroidCentralPodcast",
                    "http://radiofrance-podcast.net/podcast09/rss_13104.xml",
                    "http://radiofrance-podcast.net/podcast09/rss_12581.xml"
            );
            return false;
        }
    };

    private final ParseHelper.OnParseFinishedListener onParseFinishedListener = new ParseHelper.OnParseFinishedListener() {
        @Override
        public void onParseFinished(Channel channel) {
            Toast.makeText(DebugActivity.this, "Channel : " + channel.getTitle() + " persisted", Toast.LENGTH_SHORT).show();
        }
    };

    private final Preference.OnPreferenceClickListener deleteData = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            new DatabaseCleaner(new ContentProviderOperationExecutable(getContentResolver())).deleteTestData();
            Toast.makeText(DebugActivity.this, "Deevon dropped the tables!!!", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private final Preference.OnPreferenceClickListener addToPlaylist = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            ItemToPlaylist itemToPlaylist = new ItemToPlaylist(1, 0L);
            new AddToPlaylistPersister(getContentResolver()).persist(itemToPlaylist);
            Toast.makeText(DebugActivity.this, "Added to playlist", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());


    private final Preference.OnPreferenceClickListener searchItunes = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final SearchResult search;
                    try {
                        search = new ItunesSearch().search("bbc");
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                for (Result result : search.getResults()) {
                                    Toast.makeText(DebugActivity.this, result.getChannelOwner(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    } catch (ItunesSearch.ItunesSearchException e) {
                        e.printStackTrace();
                        HANDLER.post(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DebugActivity.this, "oopsies... search dun goofed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }).start();
            return false;
        }
    };

}
