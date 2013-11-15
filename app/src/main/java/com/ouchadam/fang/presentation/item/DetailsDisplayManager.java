package com.ouchadam.fang.presentation.item;

import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

public class DetailsDisplayManager {

    private final SlidingPanelExposer panelController;
    private final NavigatorForResult navigator;

    public DetailsDisplayManager(SlidingPanelExposer panelController, NavigatorForResult navigator) {
        this.panelController = panelController;
        this.navigator = navigator;
    }

    public void displayItem(long itemId) {
        if (isAlreadySelected(itemId)) {
            panelController.showExpanded();
        } else {
            showItemDetails(itemId, panelController.getId());
        }
    }

    private void showItemDetails(long itemId, long panelId) {
        navigator.toItemDetails(itemId, panelId);
    }

    private boolean isAlreadySelected(long clickedId) {
        return panelController.getId() == clickedId;
    }
}
