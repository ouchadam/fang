package com.ouchadam.fang.presentation.controller;

import android.view.View;

import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshAttacher;

public interface PullToRefreshExposer {
    void setRefreshing(boolean refreshing);
    void setRefreshComplete();
    void addRefreshableView(View view, PullToRefreshAttacher.OnRefreshListener onRefreshListener);
    void setEnabled(boolean canRefresh);
}
