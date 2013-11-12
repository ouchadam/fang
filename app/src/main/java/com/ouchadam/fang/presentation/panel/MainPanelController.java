package com.ouchadam.fang.presentation.panel;

import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.FangDuration;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.domain.item.Item;
import com.ouchadam.fang.presentation.item.HeroManager;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class MainPanelController {

    private final SlidingUpPanelLayout panelLayout;
    private final DurationFormatter durationFormatter;
    private final HeroManager heroManager;

    MainPanelController(SlidingUpPanelLayout panelLayout, DurationFormatter durationFormatter, HeroManager heroManager) {
        this.panelLayout = panelLayout;
        this.durationFormatter = durationFormatter;
        this.heroManager = heroManager;
        heroManager.loadDimensions();
        hidePanel();
    }

    void hidePanel() {
        panelLayout.hidePanel();
    }

    void showPanel() {
        panelLayout.showPanel();
    }

    public void updateFrom(FullItem fullItem) {
        Item item = fullItem.getItem();
        setBarTitle(item.getTitle());
        setDescription(item.getSummary());
        setDuration(item.getDuration());
        setBarSubtitle(fullItem.getChannelTitle());
        heroManager.setBackgroundImage(fullItem);
    }

    private void setBarTitle(CharSequence text) {
        setTextViewText(text, R.id.bar_title);
    }

    private void setBarSubtitle(CharSequence text) {
        setTextViewText(text, R.id.bar_sub_title);
    }

    private void setDescription(CharSequence summary) {
        setTextViewText(summary, R.id.item_description);
    }

    private void setDuration(FangDuration duration) {
        String formattedDuration = durationFormatter.format(duration);
        setTextViewText(formattedDuration, R.id.item_duration);
    }

    private void setTextViewText(CharSequence text, int viewId) {
        TextView textView = Views.findById(panelLayout, viewId);
        textView.setText(text);
    }

    public void close() {
        panelLayout.collapsePane();
    }

    public boolean isExpanded() {
        return panelLayout.isExpanded();
    }

    public void expand() {
        showPanel();
        panelLayout.expandPane();
    }

    public void setPanelSlideListener(SlidingUpPanelLayout.PanelSlideListener panelSlideListener) {
        panelLayout.setPanelSlideListener(panelSlideListener);
    }

    public void makeTopBarTransparent() {
        int color = getColour(R.color.trans_white);
        setTopBarColor(color);
    }

    private int getColour(int colourId) {
        return panelLayout.getResources().getColor(colourId);
    }

    public void makeTopBarSolid() {
        setTopBarColor(getColour(R.color.white));
    }

    private void setTopBarColor(int color) {
        View topBarContainer = Views.findById(panelLayout, R.id.top_bar_container);
        Views.findById(topBarContainer, R.id.player_top_bar).setBackgroundColor(color);
    }

    public void showBottomBar(boolean downloaded) {
        if (downloaded) {
            setBottomBarVisibility(View.VISIBLE);
        } else {
            setBottomBarVisibility(View.INVISIBLE);
        }
    }

    private void setBottomBarVisibility(int visibility) {
        Views.findById(panelLayout, R.id.player_main).setVisibility(visibility);
    }
}
