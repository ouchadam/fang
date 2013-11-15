package com.ouchadam.fang.presentation.panel;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;

class OverflowController implements PanelComponent, PopupMenu.OnMenuItemClickListener {

    private final ImageButton overflowButton;
    private final OverflowCallback overflowCallback;
    private PopupMenu popupMenu;

    private FullItem fullItem;

    OverflowController(ImageButton overflowButton, OverflowCallback overflowCallback) {
        this.overflowButton = overflowButton;
        this.overflowCallback = overflowCallback;
    }

    public void initOverflow(FullItem fullItem) {
        this.fullItem = fullItem;
        popupMenu = new PopupMenu(overflowButton.getContext(), overflowButton);
        popupMenu.inflate(R.menu.details_drawer);
        popupMenu.getMenu().findItem(R.id.ab_mark_heard).setVisible(!fullItem.isListenedTo());
        popupMenu.setOnMenuItemClickListener(this);
        overflowButton.setOnClickListener(onOverflowClicked);
    }

    private final View.OnClickListener onOverflowClicked = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            popupMenu.show();
        }
    };

    @Override
    public void showExpanded(boolean isDownloaded) {
        overflowButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCollapsed(boolean isDownloaded) {
        overflowButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.ab_mark_heard :
                overflowCallback.onMarkHeard(fullItem);
                return true;

            case R.id.ab_remove_podcast :
                overflowCallback.onRemove(fullItem);
                return true;

            case R.id.ab_go_to_channel :
                overflowCallback.onGoToChannel(fullItem);
                return true;

            case R.id.ab_close_panel :
                overflowCallback.onDismissDrawer();
                return true;

            default:
                return false;
        }
    }

}
