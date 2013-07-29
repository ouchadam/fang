package com.ouchadam.sprsrspodcast.presentation;

import android.content.res.Configuration;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.novoda.notils.android.Views;
import com.ouchadam.sprsrspodcast.R;

public class FangDrawer implements DrawerManipulator {

    private final DrawerLayout drawerLayout;
    private final ListView drawerList;
    private final FangDrawerToggle drawerToggle;

    private OnDrawItemClickListener onDrawItemClickListener;

    public static FangDrawer newInstance(FragmentActivity activity) {
        DrawerLayout drawerLayout = Views.findById(activity, R.id.drawer_layout);
        ListView drawerList = Views.findById(activity, R.id.left_drawer);

        FangDrawerToggle drawerToggle = new FangDrawerToggle(activity, drawerLayout, "Drawer");
        drawerLayout.setDrawerListener(drawerToggle);

        return new FangDrawer(drawerLayout, drawerList, drawerToggle);
    }

    FangDrawer(DrawerLayout drawerLayout, ListView drawerList, FangDrawerToggle drawerToggle) {
        this.drawerLayout = drawerLayout;
        this.drawerList = drawerList;
        this.drawerToggle = drawerToggle;
    }

    public void setAdapter(ListAdapter adapter) {
        drawerList.setAdapter(adapter);
    }

    public void setOnDrawItemClickListener(OnDrawItemClickListener onDrawItemClickListener) {
        this.onDrawItemClickListener = onDrawItemClickListener;
        drawerList.setOnItemClickListener(drawListClickListener);
    }

    private final ListView.OnItemClickListener drawListClickListener = new ListView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            onDrawItemClickListener.onDrawItemClick(FangDrawer.this, position);
        }
    };

    @Override
    public void close() {
        drawerLayout.closeDrawer(drawerList);
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

}
