package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.novoda.notils.caster.Classes;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;

public class DetailsFragment extends Fragment {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private final MenuItemManager menuItemManager;

    private Downloader downloader;
    private ItemQueryer itemQueryer;

    private FullItem item;
    private DetailsViewManager detailsViewManager;

    public static DetailsFragment newInstance(long itemId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", itemId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public DetailsFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
        this.menuItemManager = new MenuItemManager();
        this.item = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        downloader = Classes.from(activity);
        menuItemManager.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.details, menu);

        boolean isDownloading = menuItemManager.isDownloading();
        boolean isDownloaded = menuItemManager.isDownloaded();
        MenuItem downloadItem = menu.findItem(R.id.ab_download);
        downloadItem.setEnabled(!isDownloading);
        downloadItem.setVisible(!isDownloaded);

        MenuItem playItem = menu.findItem(R.id.ab_play);
        playItem.setVisible(isDownloaded);
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
                Toast.makeText(getActivity(), "Todo...", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadCurrent() {
        try {
            FullItem item = getItem();
            ItemDownloader itemDownloader = new ItemDownloader(downloader, getActivity());
            itemDownloader.setWatchers(new MenuWatcher.LazyMenuWatcher(menuItemManager));
            itemDownloader.downloadItem(item.getItem());
        } catch (IllegalAccessException e) {
            Toast.makeText(getActivity(), "Oops... no podcast found!", Toast.LENGTH_SHORT).show();
        }
    }

    private void playCurrent() {
        try {
            FullItem item = getItem();
            Log.d("Asked to play current : " + item.getChannelTitle() + " with playlist position : " + item.getPlaylistPosition());
            new ActivityResultHandler().returnWithPlayingItem(getActivity(), getItemId(), item.getPlaylistPosition(), "PLAYLIST");
        } catch (IllegalAccessException e) {
            Toast.makeText(getActivity(), "Oops... no podcast found!", Toast.LENGTH_SHORT).show();
        }
    }

    private FullItem getItem() throws IllegalAccessException {
        if (item != null) {
            return item;
        }
        throw new IllegalAccessException("Podcast has not been set, but has been asked for");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        detailsViewManager = DetailsViewManager.from(root);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarTitleSetter.set("Details");
        detailsViewManager.loadHeroDimensions();
        downloader.restore(new MenuWatcher.LazyMenuWatcher(menuItemManager));
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
            DetailsFragment.this.item = item;
            detailsViewManager.initialiseViews(item);
            initActionBarFrom(item);
        }
    };

    private void initActionBarFrom(FullItem item) {
        menuItemManager.initFrom(item);
    }

}
