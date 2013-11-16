package com.ouchadam.fang.presentation.drawer;

import android.support.v4.app.FragmentManager;

import com.ouchadam.fang.R;

public class DrawerNavigator implements OnDrawItemClickListener {

    private final FragmentManager fragmentManager;

    public DrawerNavigator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onDrawItemClick(DrawerManipulator drawer, int position) {
        selectItem(drawer, position);
    }

    private void selectItem(DrawerManipulator drawer, int position) {
        fragmentManager.beginTransaction().replace(R.id.content_frame, Fragments.get(position)).commit();
        drawer.setChecked(position, true);
        drawer.close();
    }

    public String[] toArray() {
        return Fragments.toStringList();
    }

}
