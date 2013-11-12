package com.ouchadam.fang.presentation.panel;

import android.content.DialogInterface;
import android.view.View;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;

class OverflowController implements PanelComponent {

    private final ImageButton overflowButton;
    private PopupMenu popupMenu;

    OverflowController(ImageButton overflowButton) {
        this.overflowButton = overflowButton;
    }

    public void initOverflow(FullItem fullItem) {
        popupMenu = new PopupMenu(overflowButton.getContext(), overflowButton);
        popupMenu.inflate(R.menu.details_drawer);
        popupMenu.getMenu().findItem(R.id.ab_mark_heard).setVisible(!fullItem.isListenedTo());
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

}
