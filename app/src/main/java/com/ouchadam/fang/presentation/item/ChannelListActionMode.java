package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.ouchadam.fang.R;

class ChannelListActionMode implements ActionMode.Callback {

    private final Activity activity;
    private final OnChannelListActionMode onChannelListActionMode;
    private ActionMode actionMode;

    public interface OnChannelListActionMode {
        void onDelete();

        void onActionModeFinish();
    }

    ChannelListActionMode(Activity activity, OnChannelListActionMode onChannelListActionMode) {
        this.activity = activity;
        this.onChannelListActionMode = onChannelListActionMode;
    }

    public void onStart() {
        actionMode = activity.startActionMode(this);
    }

    public boolean isInActionMode() {
        return actionMode != null;
    }

    @Override
    public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
        MenuInflater inflater = actionMode.getMenuInflater();
        inflater.inflate(R.menu.action_mode_playlist, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.ab_delete:
                onChannelListActionMode.onDelete();
                break;
            default:
                return false;
        }
        actionMode.finish();
        return true;
    }

    @Override
    public void onDestroyActionMode(ActionMode actionMode) {
        this.actionMode = null;
        onChannelListActionMode.onActionModeFinish();
    }
}
