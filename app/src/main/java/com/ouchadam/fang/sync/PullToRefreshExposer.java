package com.ouchadam.fang.sync;

public interface PullToRefreshExposer {
    void setRefreshing();
    void refreshComplete();
}
