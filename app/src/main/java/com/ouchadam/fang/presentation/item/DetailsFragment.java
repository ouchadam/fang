package com.ouchadam.fang.presentation.item;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_details, container, false);
        heroImage = Views.findById(root, R.id.content_image);
        descriptionText = Views.findById(root, R.id.fragment_item_description);
        durationText = Views.findById(root, R.id.fragment_item_duration);
        return root;
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
        if (url != null) {
            // TODO get the layout size to as the view isn't always measured in time
            Picasso.with(getActivity()).load(url).centerCrop().resize(heroImage.getWidth(), heroImage.getHeight()).into(heroImage);
        }
        // TODO load a default image or something
    }

}
