package com.ouchadam.fang.presentation.controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.drawer.ActionBarRefresher;
import com.ouchadam.fang.presentation.drawer.DrawerNavigator;
import com.ouchadam.fang.presentation.drawer.FangDrawer;
import com.ouchadam.fang.view.SlidingUpPanelLayout;

public class FangActivity extends FragmentActivity implements ActionBarRefresher, SlidingPanelExposer {

    private FangDrawer fangDrawer;
    private SlidingPanelController slidingPanelController;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (fangDrawer.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public final void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer);
        SlidingUpPanelLayout slidingPanel = Views.findById(this, R.id.sliding_layout);
        slidingPanelController = new SlidingPanelController(this, getSupportLoaderManager(), new SlidingPanelViewManipulator(slidingPanel));
        String[] strings = new String[]{"Latest", "Channels", "Playlist"};
        initDrawer(strings);
        onFangCreate(savedInstanceState);
    }

    protected void onFangCreate(Bundle savedInstanceState) {
        // template
    }

    private void initDrawer(String[] strings) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setHomeButtonEnabled(true);

        DrawerNavigator drawerNavigator = new DrawerNavigator(getSupportFragmentManager());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.drawer_list_item, strings);

        initDrawer(adapter, drawerNavigator);
    }

    private void initDrawer(ListAdapter listAdapter, DrawerNavigator drawerNavigator) {
        fangDrawer = FangDrawer.newInstance(this);
        fangDrawer.setOnDrawItemClickListener(drawerNavigator);
        fangDrawer.setAdapter(listAdapter);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        fangDrawer.onPostCreate();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        fangDrawer.onConfigurationChanged(newConfig);
    }

    @Override
    public void refresh() {
        invalidateOptionsMenu();
    }

    @Override
    public void setData(int itemColumnId) {
        slidingPanelController.setData(itemColumnId);
    }
}