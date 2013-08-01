package com.ouchadam.fang.debug;

import android.os.Bundle;
import android.preference.Preference;
import android.widget.Toast;
import com.ouchadam.bookkeeper.BookKeeper;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.PlaylistItem;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.persistance.*;

public class DebugActivity extends BasePreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromXml(R.xml.debug_layout);
        setPreferenceListener("persist_local_data", persistData);
        setPreferenceListener("delete_tables", deleteData);
        setPreferenceListener("add_to_playlist", addToPlaylist);
    }

    private final Preference.OnPreferenceClickListener persistData = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {
            new DatabaseCleaner(new ContentProviderOperationExecutable(getContentResolver())).deleteTestData();
            ParseHelper parseHelper = new ParseHelper(onParseFinishedListener);
            parseHelper.parse(DebugActivity.this, "feed_cnet_small.xml");
            parseHelper.parse(DebugActivity.this, "feed_hsw_small.xml");
            return false;
        }
    };

    private final ParseHelper.OnParseFinishedListener onParseFinishedListener = new ParseHelper.OnParseFinishedListener() {
        @Override
        public void onParseFinished(Channel channel) {
            new ChannelPersister(getContentResolver()).persist(channel);
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
            PlaylistItem playlistItem = new PlaylistItem(1);
            new AddToPlaylistPersister(getContentResolver()).persist(playlistItem);
            Toast.makeText(DebugActivity.this, "Added to playlist", Toast.LENGTH_SHORT).show();
            return false;
        }
    };

}