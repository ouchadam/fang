package com.ouchadam.fang.presentation.controller;

import android.view.View;
import android.widget.TextView;
import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

class SlidingPanelViewManipulator {

    private final SlidingUpPanelLayout panelLayout;

    SlidingPanelViewManipulator(SlidingUpPanelLayout panelLayout) {
        this.panelLayout = panelLayout;
    }

    public void setTopBarPlayClicked(View.OnClickListener onTopBarPlayClicked) {
        panelLayout.findViewById(R.id.top_bar_play).setOnClickListener(onTopBarPlayClicked);
    }

    public void setBarText(CharSequence text) {
        TextView barTitle = Views.findById(panelLayout, R.id.bar_title);
        barTitle.setText(text);
    }
}
