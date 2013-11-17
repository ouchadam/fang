package com.ouchadam.fang.presentation.panel;

import android.view.View;
import android.widget.ViewSwitcher;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;

class MediaViewController implements PanelComponent {

    private final View playTop;
    private final View playBottom;
    private final View pauseTop;
    private final View pauseBottom;
    private final View rewind;
    private final View fastForward;
    private final View next;
    private final View previous;
    private final ViewSwitcher topMediaSwitcher;
    private final ViewSwitcher bottomMediaSwitcher;

    MediaViewController(View playTop, View playBottom, View pauseTop, View pauseBottom, View rewind, View fastForward, View next, View previous, ViewSwitcher topMediaSwitcher, ViewSwitcher bottomMediaSwitcher) {
        this.playTop = playTop;
        this.playBottom = playBottom;
        this.pauseTop = pauseTop;
        this.pauseBottom = pauseBottom;
        this.rewind = rewind;
        this.fastForward = fastForward;
        this.next = next;
        this.previous = previous;
        this.topMediaSwitcher = topMediaSwitcher;
        this.bottomMediaSwitcher = bottomMediaSwitcher;
    }

    public void showPlay() {
        if (isInPauseState()) {
            showNext();
        }
    }

    public void showPause() {
        if (isInPlayState()) {
            showNext();
        }
    }

    private boolean isInPlayState() {
        return topMediaSwitcher.getCurrentView().getId() == R.id.play_top;
    }


    private boolean isInPauseState() {
        return topMediaSwitcher.getCurrentView().getId() == R.id.pause_top;
    }

    public void setMediaVisibility(boolean isExpanded, FullItem fullItem) {
        topMediaSwitcher.setVisibility(fullItem.isDownloaded() && !isExpanded ? View.VISIBLE : View.INVISIBLE);
        bottomMediaSwitcher.setVisibility(fullItem.isDownloaded() ? View.VISIBLE : View.INVISIBLE);
    }

    public void setPlayPauseListeners(View.OnClickListener onPlayClicked, View.OnClickListener onPauseClicked) {
        playTop.setOnClickListener(onPlayClicked);
        playBottom.setOnClickListener(onPlayClicked);
        pauseTop.setOnClickListener(onPauseClicked);
        pauseBottom.setOnClickListener(onPauseClicked);
    }

    public void setRewindFastForwardListeners(View.OnClickListener onRewind, View.OnClickListener onFastForward) {
        rewind.setOnClickListener(onRewind);
        fastForward.setOnClickListener(onFastForward);
    }

    public void setNextPreviousListeners(View.OnClickListener onNextClicked, View.OnClickListener onPreviousClicked) {
        next.setOnClickListener(onNextClicked);
        previous.setOnClickListener(onPreviousClicked);
    }

    public void showNext() {
        topMediaSwitcher.showNext();
        bottomMediaSwitcher.showNext();
    }

    @Override
    public void showExpanded(boolean downloaded) {
        topMediaSwitcher.setVisibility(View.INVISIBLE);
        if (downloaded) {
            bottomMediaSwitcher.setVisibility(View.VISIBLE);
        } else {
            bottomMediaSwitcher.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void showCollapsed(boolean downloaded) {
        if (downloaded) {
            topMediaSwitcher.setVisibility(View.VISIBLE);
        } else {
            topMediaSwitcher.setVisibility(View.INVISIBLE);
        }
    }
}
