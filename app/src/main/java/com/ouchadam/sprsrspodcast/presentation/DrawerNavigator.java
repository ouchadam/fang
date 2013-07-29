package com.ouchadam.sprsrspodcast.presentation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ouchadam.sprsrspodcast.R;

public class DrawerNavigator implements OnDrawItemClickListener {

    private final FragmentManager fragmentManager;

    DrawerNavigator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onDrawItemClick(DrawerManipulator drawer, int position) {
        selectItem(drawer, position);
    }

    private void selectItem(DrawerManipulator drawer, int position) {
        Fragment fragment = new ItemList();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
        drawer.setChecked(position, true);
        drawer.setOnCloseTitle("Item list");
        drawer.close();
    }

}
