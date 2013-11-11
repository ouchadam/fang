package com.ouchadam.fang.presentation.search;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.api.search.ItunesSearch;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.api.search.SearchResult;
import com.ouchadam.fang.debug.ParseHelper;
import com.ouchadam.fang.domain.channel.Channel;
import com.ouchadam.fang.presentation.item.ActionBarTitleSetter;

import java.util.List;

public class ExploreFragment extends Fragment implements AdapterView.OnItemClickListener, ParseHelper.OnParseFinishedListener {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());
    private final ActionBarTitleSetter actionBarTitleSetter;
    private SearchAdapter adapter;

    public static ExploreFragment newInstance(String query) {
        Bundle arguments = new Bundle();
        arguments.putString("Query", query);
        ExploreFragment exploreFragment = new ExploreFragment();
        exploreFragment.setArguments(arguments);
        return exploreFragment;
    }

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
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        ListView listView = Views.findById(root, R.id.list);
        adapter = new SearchAdapter(LayoutInflater.from(getActivity()), getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setHasOptionsMenu(true);
        actionBarTitleSetter.set("Explore");
        if (hasQuery()) {
            searchFor(getSearchQuery());
        }
    }

    private boolean hasQuery() {
        return getArguments() != null && getArguments().containsKey("Query");
    }

    private String getSearchQuery() {
        return getArguments().getString("Query");
    }

    public void searchFor(final String searchTerm) {
        updateUi(searchTerm);
        performSearch(searchTerm);
    }

    private void updateUi(String searchTerm) {
        // TODO
    }

    private void performSearch(final String searchTerm) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SearchResult search;
                try {
                    search = new ItunesSearch().search(searchTerm);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            updateAdapter(search.getResults());
                        }
                    });
                } catch (ItunesSearch.ItunesSearchException e) {
                    e.printStackTrace();
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "oopsies... search dun goofed", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        }).start();
    }

    private void updateAdapter(List<Result> results) {
        adapter.updateData(results);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        Result item = adapter.getItem(position);
        dirtyParsing(item);
    }

    private void dirtyParsing(Result item) {
        ParseHelper parseHelper = new ParseHelper(getActivity().getContentResolver(), this);
        parseHelper.parse(getActivity(), item.getFeedUrl());
    }

    @Override
    public void onParseFinished(Channel channel) {
        Toast.makeText(getActivity(), "Added : " + channel.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
