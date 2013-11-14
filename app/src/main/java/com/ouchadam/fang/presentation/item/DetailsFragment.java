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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.ouchadam.bookkeeper.Downloader;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.presentation.panel.DurationFormatter;

public class DetailsFragment extends Fragment {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private Downloader downloader;

    private ItemQueryer itemQueryer;
    private TextView descriptionText;
    private TextView durationText;
    private ImageView heroImage;

    private boolean isDownloaded;
    private FullItem item;
    private HeroManager heroManager;
    private TextView channelText;
    private TextView itemTitleText;

    public static DetailsFragment newInstance(long itemId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", itemId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public DetailsFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
        this.isDownloaded = false;
        this.item = null;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setHasOptionsMenu(true);
        downloader = Classes.from(activity);
        actionBarTitleSetter.onAttach(activity);
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
                Toast.makeText(getActivity(), "Todo...", Toast.LENGTH_SHORT).show();

        }
        return super.onOptionsItemSelected(item);
    }

    private void downloadCurrent() {
        FullItem item = getItem();
        if (item != null) {
            ItemDownloader itemDownloader = new ItemDownloader(downloader, getActivity());
            itemDownloader.downloadItem(item.getItem());
        }
    }

    private FullItem getItem() {
        return item;
    }

    private void playCurrent() {
        FullItem item = getItem();
        new ActivityResultHandler().returnWithPlayingItem(getActivity(), getItemId(), item.getPlaylistPosition(), "PLAYLIST");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        heroImage = Views.findById(root, R.id.content_image);
        descriptionText = Views.findById(root, R.id.fragment_item_description);
        durationText = Views.findById(root, R.id.fragment_item_duration);
        channelText = Views.findById(root, R.id.fragment_channel_title);
        itemTitleText = Views.findById(root, R.id.fragment_item_title);
        Views.findById(root, R.id.details_parent).setOnClickListener(detailsClickListener);
        return root;
    }

    private final View.OnClickListener detailsClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO make this a setting
            View container = Views.findById(view, R.id.details_container);
            toggleVisibility(container);
        }
    };

    private void toggleVisibility(View view) {
        int visibility = view.getVisibility();
        view.setVisibility(getReversedVisibility(visibility));
    }

    private int getReversedVisibility(int visibility) {
        validateVisibilty(visibility);
        if (visibility == View.INVISIBLE || visibility == View.GONE) {
            return View.VISIBLE;
        } else {
            return View.INVISIBLE;
        }
    }

    private void validateVisibilty(int visibility) {
        if (visibility != View.GONE && visibility != View.INVISIBLE && visibility != View.VISIBLE) {
            throw new IllegalArgumentException("Must use one of the android specified visibilities");
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        actionBarTitleSetter.set("Details");
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
        channelText.setText(item.getChannelTitle());
        itemTitleText.setText(baseItem.getTitle());
        descriptionText.setText(baseItem.getSummary());
        durationText.setText(new DurationFormatter(getResources()).format(baseItem.getDuration()));
        heroManager.setBackgroundImage(item);
    }

    private void initActionBarFrom(FullItem item) {
        isDownloaded = item.isDownloaded();
        getActivity().invalidateOptionsMenu();
    }

}
