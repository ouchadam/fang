package com.ouchadam.sprsrspodcast.presentation.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.presentation.item.ChannelFragment;
import com.ouchadam.sprsrspodcast.presentation.item.LatestFragment;

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
        DrawerItem drawerItem = DrawerItem.get(position);
        fragmentManager.beginTransaction().replace(R.id.content_frame, drawerItem.fragment).commit();
        drawer.setChecked(position, true);
        drawer.setOnCloseTitle(drawerItem.title);
        drawer.close();
    }

    private static class DrawerItem {

        private final Fragment fragment;
        private final String title;

        private DrawerItem(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        public static DrawerItem get(int position) {
            if (position == 0) {
                return new DrawerItem(new LatestFragment(), "Latest");
            }
            return new DrawerItem(new ChannelFragment(), "Channels");
        }

    }

}
