package com.ouchadam.fang.presentation.panel;

import com.ouchadam.fang.domain.FullItem;

public interface OverflowCallback {
    void onMarkHeard(FullItem fullItem);
    void onGoToDetails(FullItem fullItem);
    void onRemove(FullItem fullItem);
    void onDismissDrawer();
}
