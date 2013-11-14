package com.ouchadam.fang.presentation;

import android.database.Cursor;

import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.PodcastPosition;
import com.ouchadam.fang.domain.channel.Image;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.database.Tables;
import novoda.android.typewriter.cursor.CursorMarshaller;

public class FullItemMarshaller implements CursorMarshaller<FullItem> {

    @Override
    public FullItem marshall(Cursor cursor) {
        CursorUtils cursorUtil = new CursorUtils(cursor);
        Item item = new ItemMarshaller().marshall(cursor);
        Image image = new ImageCursorMarshaller().marshall(cursor);
        String channelTitle = cursorUtil.getString(Tables.Channel.CHANNEL_TITLE);
        long downloadId = cursorUtil.getLong(Tables.Playlist.DOWNLOAD_ID);
        boolean isDownloaded = cursorUtil.getBoolean(Tables.Playlist.DOWNLOADED);
        int playPosition = cursorUtil.getInt(Tables.Playlist.PLAY_POSITION);
        int maxDuration = cursorUtil.getInt(Tables.Playlist.MAX_DURATION);
        int playlistPosition = cursorUtil.getInt(Tables.Playlist.LIST_POSITION);
        int newItemCount = cursorUtil.getInt(Tables.Channel.NEW_ITEM_COUNT);

        PodcastPosition podcastPosition = new PodcastPosition(playPosition, maxDuration);

        return new FullItem(item, channelTitle, image, downloadId, isDownloaded, podcastPosition, playlistPosition, newItemCount);
    }

    private static class ImageCursorMarshaller implements CursorMarshaller<Image> {
        @Override
        public Image marshall(Cursor cursor) {
            CursorUtils cursorUtil = new CursorUtils(cursor);
            String url = cursorUtil.getString(Tables.ChannelImage.IMAGE_URL);
            String link = cursorUtil.getString(Tables.ChannelImage.LINK);
            String title = cursorUtil.getString(Tables.ChannelImage.TITLE);
            int width = cursorUtil.getInt(Tables.ChannelImage.WIDTH);
            int height = cursorUtil.getInt(Tables.ChannelImage.HEIGHT);
            return new Image(url, link, title, width, height);
        }
    }

}
