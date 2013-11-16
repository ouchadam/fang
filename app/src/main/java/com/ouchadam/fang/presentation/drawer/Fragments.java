package com.ouchadam.fang.presentation.drawer;

import android.support.v4.app.Fragment;

import com.ouchadam.fang.presentation.item.ChannelFragment;
import com.ouchadam.fang.presentation.item.LatestFragment;
import com.ouchadam.fang.presentation.item.PlaylistFragment;
import com.ouchadam.fang.presentation.search.ExploreFragment;

public enum Fragments {
    LATEST {
        @Override
        public Fragment get() {
            return new LatestFragment();
        }
    },
    CHANNELS {
        @Override
        public Fragment get() {
            return new ChannelFragment();
        }
    },
    PLAYLIST {
        @Override
        public Fragment get() {
            return new PlaylistFragment();
        }
    },
    EXPLORE {
        @Override
        public Fragment get() {
            return new ExploreFragment();
        }
    };

    public abstract Fragment get();

    public static Fragment get(int position) {
        return values()[position].get();
    }

    public static String[] toStringList() {
        return new String[]{"Latest", "Channels", "Playlist", "Explore"};
    }
}
