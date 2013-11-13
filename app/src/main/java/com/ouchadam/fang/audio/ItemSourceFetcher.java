package com.ouchadam.fang.audio;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import com.ouchadam.fang.domain.FullItem;

public class ItemSourceFetcher {

    private final DownloadManager downloadManager;

    static ItemSourceFetcher from(Context context) {
        return new ItemSourceFetcher((DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE));
    }

    ItemSourceFetcher(DownloadManager downloadManager) {
        this.downloadManager = downloadManager;
    }

    public Uri getSourceUri(long downloadId) {
        return downloadManager.getUriForDownloadedFile(downloadId);
    }

}
