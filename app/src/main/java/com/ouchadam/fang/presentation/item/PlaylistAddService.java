package com.ouchadam.fang.presentation.item;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;

public class PlaylistAddService extends IntentService {

    static final String ACTION = "playlistAddService";
    static final String EXTRA_PLAYLIST_ITEM = "key";

    private static final int NEXT_POSITION_OFFSET = 1;
    private DatabaseCounter databaseCounter;


    static void start(Context context, ItemToPlaylist itemToPlaylist) {
        context.startService(createIntent(context, itemToPlaylist));
    }

    private static Intent createIntent(Context context, ItemToPlaylist itemToPlaylist) {
        Intent intent = new Intent(context, PlaylistAddService.class);
        intent.setAction(ACTION);
        intent.putExtra(EXTRA_PLAYLIST_ITEM, itemToPlaylist);
        return intent;
    }

    public PlaylistAddService() {
        super("PlaylistAddService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (validIntent(intent)) {
            this.databaseCounter = new DatabaseCounter(getContentResolver(), Uris.PLAYLIST, new String[]{Tables.Playlist.ITEM_ID.name()}, null, null);
            addToPlaylist(getItemFromIntent(intent.getExtras()));
        }
    }

    private boolean validIntent(Intent intent) {
        return ACTION.equals(intent.getAction()) && intent.getExtras() != null;
    }

    private ItemToPlaylist getItemFromIntent(Bundle extras) {
        return Classes.from(extras.getSerializable(EXTRA_PLAYLIST_ITEM));
    }

    private void addToPlaylist(final ItemToPlaylist item) {
        int listPosition = getPlaylistTotal() + NEXT_POSITION_OFFSET;
        item.setListPosition(listPosition);
        new AddToPlaylistPersister(getContentResolver()).persist(item);
    }

    private int getPlaylistTotal() {
        return databaseCounter.getCurrentCount();
    }

}
