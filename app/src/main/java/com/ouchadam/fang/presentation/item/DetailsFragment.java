package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.ItemQueryer;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.presentation.panel.DurationFormatter;
import com.squareup.picasso.Picasso;

public class DetailsFragment extends Fragment {

    private final HeroHolder heroHolder;

    private ItemQueryer itemQueryer;
    private TextView descriptionText;
    private TextView durationText;
    private ImageView heroImage;

    public static DetailsFragment newInstance(long itemId) {
        DetailsFragment detailsFragment = new DetailsFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("itemId", itemId);
        detailsFragment.setArguments(bundle);
        return detailsFragment;
    }

    public DetailsFragment() {
        heroHolder = new HeroHolder();
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
        getHeroDimensions(heroImage);
    }

    private void getHeroDimensions(final ImageView heroImage) {
        final ViewTreeObserver treeObserver = heroImage.getViewTreeObserver();
        if (treeObserver != null && treeObserver.isAlive()) {
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    heroHolder.width = heroImage.getWidth();
                    heroHolder.height = heroImage.getHeight();
                    heroHolder.tryLoad(getActivity(), heroImage);
                    heroImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        long itemId = getArguments().getLong("itemId");
        setData(itemId);
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
        Item baseItem = item.getItem();
        item.getChannelTitle();
        baseItem.getTitle();
        descriptionText.setText(baseItem.getSummary());
        durationText.setText(new DurationFormatter(getResources()).format(baseItem.getDuration()));
        setBackgroundImage(getImageUrl(item));
    }

    private String getImageUrl(FullItem fullItem) {
        String heroImage = fullItem.getItem().getHeroImage();
        String channelImage = fullItem.getImageUrl();
        return heroImage == null ? channelImage : heroImage;
    }

    private void setBackgroundImage(String url) {
        heroHolder.url = url;
        heroHolder.tryLoad(getActivity(), heroImage);
    }

    private static class HeroHolder {

        private static final int INVALID = -1;

        int width;
        int height;
        String url;

        HeroHolder() {
            this.width = INVALID;
            this.height = INVALID;
            this.url = null;
        }

        void tryLoad(Context context, ImageView imageView) {
            if (isValid(width) && isValid(height) && isValid(url)) {
                Picasso.with(context).load(url).centerCrop().resize(width, height).into(imageView);
            }
        }

        private boolean isValid(int dimen) {
            return dimen != INVALID;
        }


        private boolean isValid(String url) {
            return url != null;
        }
    }

}
