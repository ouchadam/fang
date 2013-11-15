package com.ouchadam.fang.presentation.item;

import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.presentation.panel.DurationFormatter;

class DetailsViewManager {

    private final TextView itemTitleText;
    private final TextView channelText;
    private final TextView descriptionText;
    private final TextView durationText;
    private final HeroManager heroManager;
    private Resources resources;

    static DetailsViewManager from(View root) {
        ImageView heroImage = Views.findById(root, R.id.content_image);
        HeroManager heroManager = new HeroManager(new HeroHolder(), heroImage, root.getContext());
        TextView descriptionText = Views.findById(root, R.id.fragment_item_description);
        TextView durationText = Views.findById(root, R.id.fragment_item_duration);
        TextView channelText = Views.findById(root, R.id.fragment_channel_title);
        TextView itemTitleText = Views.findById(root, R.id.fragment_item_title);
        return new DetailsViewManager(itemTitleText, channelText, descriptionText, durationText, heroManager, root.getResources());
    }

    DetailsViewManager(TextView itemTitleText, TextView channelText, TextView descriptionText, TextView durationText, HeroManager heroManager, Resources resources) {
        this.itemTitleText = itemTitleText;
        this.channelText = channelText;
        this.descriptionText = descriptionText;
        this.durationText = durationText;
        this.heroManager = heroManager;
        this.resources = resources;
    }

    void initialiseViews(final FullItem item) {
        Item baseItem = item.getItem();
        channelText.setText(item.getChannelTitle());
        itemTitleText.setText(baseItem.getTitle());
        descriptionText.setText(baseItem.getSummary());
        durationText.setText(new DurationFormatter(resources).format(baseItem.getDuration()));
        heroManager.setBackgroundImage(item);
    }

    public void loadHeroDimensions() {
        heroManager.loadDimensions();
    }
}
