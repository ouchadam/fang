package com.ouchadam.fang.presentation.item;

import android.content.Context;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import com.ouchadam.fang.domain.FullItem;

public class HeroManager {

    private final HeroHolder heroHolder;
    private final ImageView heroImage;
    private final Context context;

    public HeroManager(HeroHolder heroHolder, ImageView heroImage, Context context) {
        this.heroHolder = heroHolder;
        this.heroImage = heroImage;
        this.context = context;
    }

    public void loadDimensions() {
        final ViewTreeObserver treeObserver = heroImage.getViewTreeObserver();
        if (treeObserver != null && treeObserver.isAlive()) {
            treeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    heroHolder.width = heroImage.getWidth();
                    heroHolder.height = heroImage.getHeight();
                    heroHolder.tryLoad(context, heroImage);
                    heroImage.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
            });
        }
    }

    public void setBackgroundImage(FullItem fullItem) {
        heroHolder.url = getImageUrl(fullItem);
        heroHolder.tryLoad(context, heroImage);
    }

    private String getImageUrl(FullItem fullItem) {
        String heroImage = fullItem.getItem().getHeroImage();
        String channelImage = fullItem.getImageUrl();
        return heroImage == null ? channelImage : heroImage;
    }

}
