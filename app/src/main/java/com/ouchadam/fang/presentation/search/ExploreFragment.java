package com.ouchadam.fang.presentation.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.parsing.itunesrss.Entry;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastFeed;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastParser;
import com.ouchadam.fang.presentation.item.ActionBarTitleSetter;
import com.ouchadam.fang.presentation.item.Navigator;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ExploreFragment extends Fragment {

    private final ActionBarTitleSetter actionBarTitleSetter;

    public ExploreFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.explore_menu, menu);

        // TODO move to a different fragment
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setIconifiedByDefault(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ab_add:
                openAddDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void openAddDialog() {
        new AddFeedDialog().show(getFragmentManager(), "Add dialog");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_explore, container, false);
        Button musicButton = Views.findById(root, R.id.top_ten_music);
        musicButton.setOnClickListener(musicOnClick);
        return root;
    }

    private final View.OnClickListener musicOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new Navigator(getActivity()).toTopTen();
        }
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        actionBarTitleSetter.set("Explore");
    }

}
