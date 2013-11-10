package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.bookkeeper.domain.DownloadId;
import com.ouchadam.bookkeeper.watcher.NotificationWatcher;
import com.ouchadam.fang.ItemDownload;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.ItemToPlaylist;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.persistance.AddToPlaylistPersister;
import com.ouchadam.fang.presentation.panel.DurationFormatter;

public class DetailsFragment extends Fragment {

    private Downloader downloader;

    private ItemQueryer itemQueryer;
    private TextView descriptionText;
    private TextView durationText;
    private ImageView heroImage;

    private boolean isDownloaded;
    private FullItem item;
    private HeroManager heroManager;

    public static DetailsFragment newInstance(long itemId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", itemId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public DetailsFragment() {
        this.isDownloaded = false;
        this.item = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        downloader = Classes.from(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details, menu);
        menu.findItem(R.id.ab_play).setVisible(isDownloaded);
        menu.findItem(R.id.ab_download).setVisible(!isDownloaded);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_download:
                downloadCurrent();
                break;
            case R.id.ab_play:
                playCurrent();
                break;
            default:
                // todo nothing

        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadCurrent() {
        FullItem item = getItem();
        if (item != null) {
            ItemDownload downloadable = ItemDownload.from(item.getItem());
            DownloadId downloadId = downloader.keep(downloadable);
            downloader.store(downloadId, getItemId());

            new AddToPlaylistPersister(getActivity().getContentResolver()).persist(ItemToPlaylist.from(item.getItem(), downloadId.value()));
            downloader.watch(downloadId, new NotificationWatcher(getActivity(), downloadable, downloadId));
        }
    }

    private FullItem getItem() {
        return item;
    }

    private void playCurrent() {
        FullItem item = getItem();
        new ActivityResultHandler().returnWithPlayingItem(getActivity(), getItemId(), getSourceUri(item));
    }

    private Uri getSourceUri(FullItem item) {
        DownloadManager downloadManager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
        return downloadManager.getUriForDownloadedFile(item.getDownloadId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        heroImage = Views.findById(root, R.id.content_image);
        descriptionText = Views.findById(root, R.id.fragment_item_description);
        durationText = Views.findById(root, R.id.fragment_item_duration);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        heroManager = new HeroManager(new HeroHolder(), heroImage, getActivity());
        heroManager.loadDimensions();
    }

    @Override
    public void onResume() {
        super.onResume();
        setData(getItemId());
    }

    private long getItemId() {
        return getArguments().getLong("itemId");
    }

    private void setData(long itemId) {
        if (itemQueryer != null) {
            itemQueryer.stop();
        }
        itemQueryer = new ItemQueryer(getActivity(), itemId, getLoaderManager(), onItem);
        itemQueryer.query();
    }

    private final ItemQueryer.OnItemListener onItem = new ItemQueryer.OnItemListener() {
        @Override
        public void onItem(FullItem item) {
            initialiseViews(item);
        }
    };

    private void initialiseViews(final FullItem item) {
        this.item = item;
        Item baseItem = item.getItem();
        initActionBarFrom(item);
        item.getChannelTitle();
        baseItem.getTitle();
        descriptionText.setText(baseItem.getSummary());
        durationText.setText(new DurationFormatter(getResources()).format(baseItem.getDuration()));
        heroManager.setBackgroundImage(item);
    }

    private void initActionBarFrom(FullItem item) {
        isDownloaded = item.isDownloaded();
        getActivity().invalidateOptionsMenu();
    }

}
