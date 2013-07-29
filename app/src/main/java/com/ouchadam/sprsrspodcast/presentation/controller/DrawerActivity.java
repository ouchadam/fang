package com.ouchadam.sprsrspodcast.presentation.controller;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.presentation.drawer.ActionBarRefresher;
import com.ouchadam.sprsrspodcast.presentation.drawer.DrawerNavigator;
import com.ouchadam.sprsrspodcast.presentation.drawer.FangDrawer;

public abstract class DrawerActivity extends FragmentActivity implements ActionBarRefresher {

    private FangDrawer fangDrawer;

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

}