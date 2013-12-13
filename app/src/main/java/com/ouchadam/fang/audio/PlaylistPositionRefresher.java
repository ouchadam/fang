package com.ouchadam.fang.audio;

import android.content.ContentResolver;

import com.novoda.notils.java.Collections;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;

import java.util.List;

class PlaylistPositionRefresher {

    private final ContentResolver contentResolver;
    private final PlaylistLoader playlistLoader;

    PlaylistPositionRefresher(ContentResolver contentResolver, PlaylistLoader playlistLoader) {
        this.contentResolver = contentResolver;
        this.playlistLoader = playlistLoader;
    }

    public void refresh() {
        Log.d("Refreshing playlist");
        playlistLoader.load(refreshPlaylist);
    }

    private final PlaylistLoader.OnPlaylistLoad refreshPlaylist = new PlaylistLoader.OnPlaylistLoad() {
        @Override
        public void onPlaylistLoad(List<Playlist.PlaylistItem> playlistItems) {
            List<Playlist.PlaylistItem> fixedItems = fixOrder(playlistItems);
            List<ItemToPlaylist> itemToPlaylist = Collections.newArrayList();
            for (Playlist.PlaylistItem fixedItem : fixedItems) {
                ItemToPlaylist item = new ItemToPlaylist(fixedItem.id, fixedItem.downloadId);
                item.setListPosition(fixedItem.listPosition);
                itemToPlaylist.add(item);
            }
            new AddToPlaylistPersister(contentResolver).persist(itemToPlaylist);
        }
    };


    private List<Playlist.PlaylistItem> fixOrder(List<Playlist.PlaylistItem> playlistItems) {
        for (int index = 0; index < playlistItems.size(); index++) {
            Playlist.PlaylistItem playlistItem = playlistItems.get(index);
            Log.d("playlist item had position : " + playlistItem.listPosition + " replacing with : " + (index + 1));
            playlistItem.setListPosition(index + 1);
        }
        return playlistItems;
    }

}
