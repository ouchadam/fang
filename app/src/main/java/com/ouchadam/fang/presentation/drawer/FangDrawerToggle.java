package com.ouchadam.fang.presentation.drawer;

import android.app.ActionBar;
import android.app.Activity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.View;

import com.novoda.notils.caster.Classes;
import com.ouchadam.fang.R;

public class FangDrawerToggle extends ActionBarDrawerToggle {

    private final ActionBar actionBar;
    private final String drawerTitle;
    private final ActionBarRefresher actionBarRefresher;

    private String activityTitle;

    public FangDrawerToggle(Activity activity, DrawerLayout drawerLayout, String drawerTitle) {
        super(activity, drawerLayout, R.drawable.ic_navigation_drawer, R.string.drawer_open, R.string.drawer_close);
        this.drawerTitle = drawerTitle;
        actionBar = activity.getActionBar();
        actionBarRefresher = Classes.from(activity);
    }

    public void setOutgoingActivityTitle(String activityTitle) {
        this.activityTitle = activityTitle;
    }

    @Override
    public void onDrawerClosed(View view) {
        actionBar.setTitle(activityTitle);
        refreshActionBar();
    }

    @Override
    public void onDrawerOpened(View drawerView) {
        actionBar.setTitle(drawerTitle);
        refreshActionBar();
    }

    private void refreshActionBar() {
        actionBarRefresher.refresh();
    }
}
