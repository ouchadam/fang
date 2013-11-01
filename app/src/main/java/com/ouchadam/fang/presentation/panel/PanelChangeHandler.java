package com.ouchadam.fang.presentation.panel;

import android.view.View;

import com.ouchadam.fang.presentation.ActionBarManipulator;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

public class PanelChangeHandler implements SlidingUpPanelLayout.PanelSlideListener {

    private final ActionBarManipulator actionBarManipulator;
    private final OnPanelChangeListener onPanelChangeListener;

    PanelChangeHandler(ActionBarManipulator actionBarManipulator, OnPanelChangeListener onPanelChangeListener) {
        this.actionBarManipulator = actionBarManipulator;
        this.onPanelChangeListener = onPanelChangeListener;
    }

    @Override
    public void onPanelSlide(View panel, float slideOffset) {
        if (slideOffset < 0.2) {
            if (actionBarManipulator.isActionBarShowing()) {
                actionBarManipulator.hideActionBar();
            }
        } else {
            if (!actionBarManipulator.isActionBarShowing()) {
                actionBarManipulator.showActionBar();
            }
        }
    }

    @Override
    public void onPanelCollapsed(View panel) {
        onPanelChangeListener.onPanelCollapsed(panel);
    }

    @Override
    public void onPanelExpanded(View panel) {
        onPanelChangeListener.onPanelExpanded(panel);
    }
}
