package com.ouchadam.fang.presentation.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.item.ChannelFragment;
import com.ouchadam.fang.presentation.item.LatestFragment;
import com.ouchadam.fang.presentation.item.PlaylistFragment;
import com.ouchadam.fang.presentation.search.ExploreFragment;

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

    enum Fragments {
        LATEST {
            @Override
            Fragment get() {
                return new LatestFragment();
            }
        },
        CHANNELS {
            @Override
            Fragment get() {
                return new ChannelFragment();
            }
        },
        PLAYLIST {
            @Override
            Fragment get() {
                return new PlaylistFragment();
            }
        },
        EXPLORE {
            @Override
            Fragment get() {
                return new ExploreFragment();
            }
        };

        abstract Fragment get();

        public static Fragment get(int position) {
            return values()[position].get();
        }

        public static String[] toStringList() {
            return new String[]{"Latest", "Channels", "Playlist", "Explore"};
        }
    }

}
