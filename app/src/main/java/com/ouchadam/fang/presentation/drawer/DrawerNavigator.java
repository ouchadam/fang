package com.ouchadam.fang.presentation.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.item.ChannelFragment;
import com.ouchadam.fang.presentation.item.LatestFragment;
import com.ouchadam.fang.presentation.item.PlaylistFragment;

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

    public String[] toArray() {
        return Fragments.toStringList();
    }

    private static class DrawerItem {

        private final Fragment fragment;
        private final String title;

        private DrawerItem(Fragment fragment, String title) {
            this.fragment = fragment;
            this.title = title;
        }

        public static DrawerItem get(int position) {
            return Fragments.values()[position].get();
        }
    }

    enum Fragments {
        LATEST {
            @Override
            DrawerItem get() {
                return new DrawerItem(new LatestFragment(), "Latest");
            }
        },
        CHANNELS {
            @Override
            DrawerItem get() {
                return new DrawerItem(new ChannelFragment(), "Channels");
            }
        },
        PLAYLIST {
            @Override
            DrawerItem get() {
                return new DrawerItem(new PlaylistFragment(), "Playlist");
            }
        };

        abstract DrawerItem get();

        static String[] toStringList() {
            return new String[]{"Latest", "Channels", "Playlist"};
        }

    }

}
