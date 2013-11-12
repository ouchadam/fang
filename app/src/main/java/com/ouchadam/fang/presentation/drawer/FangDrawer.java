package com.ouchadam.fang.presentation.drawer;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;

public class FangDrawer implements DrawerManipulator, DrawerDisEnabler {

    private final DrawerLayout drawerLayout;
    private final View drawerParent;
    private final ListView drawerList;
    private final FangDrawerToggle drawerToggle;
    private final OnDrawItemClickListener onDrawItemClickListener;

    public static FangDrawer newInstance(FragmentActivity activity, DrawerNavigator drawerNavigator) {
        DrawerLayout drawerLayout = Views.findById(activity, R.id.drawer_layout);
        ListView drawerList = Views.findById(activity, R.id.left_drawer);
        View drawerParent = Views.findById(activity, R.id.drawer_content);

        FangDrawerToggle drawerToggle = new FangDrawerToggle(activity, drawerLayout, "Fang");
        drawerLayout.setDrawerListener(drawerToggle);

        return new FangDrawer(drawerLayout, drawerParent, drawerList, drawerToggle, drawerNavigator);
    }

    FangDrawer(DrawerLayout drawerLayout, View drawerParent, ListView drawerList, FangDrawerToggle drawerToggle, OnDrawItemClickListener onDrawItemClickListener) {
        this.drawerLayout = drawerLayout;
        this.drawerParent = drawerParent;
        this.drawerList = drawerList;
        this.drawerToggle = drawerToggle;
        this.onDrawItemClickListener = onDrawItemClickListener;
        setOnDrawItemClickListener();
    }

    private void setOnDrawItemClickListener() {
        drawerList.setOnItemClickListener(drawListClickListener);
    }

    private final ListView.OnItemClickListener drawListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            onDrawItemClickListener.onDrawItemClick(FangDrawer.this, position);
        }
    };

    public void setAdapter(ListAdapter adapter) {
        drawerList.setAdapter(adapter);
    }

    @Override
    public void close() {
        drawerLayout.closeDrawer(drawerParent);
    }

    @Override
    public void setChecked(int position, boolean checked) {
        drawerList.setItemChecked(position, checked);
    }

    @Override
    public void setOnCloseTitle(String title) {
        drawerToggle.setOutgoingActivityTitle(title);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return drawerToggle.onOptionsItemSelected(item);
    }

    public void onPostCreate() {
        drawerToggle.syncState();
    }

    public void onConfigurationChanged(Configuration newConfig) {
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void enable() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void disable() {
        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
    }
}
