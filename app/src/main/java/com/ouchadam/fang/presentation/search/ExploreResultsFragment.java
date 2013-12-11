package com.ouchadam.fang.presentation.search;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.debug.ParseHelper;
import com.ouchadam.fang.presentation.item.ActionBarTitleSetter;

import java.util.List;

public class ExploreResultsFragment extends Fragment implements AdapterView.OnItemClickListener, ItunesApiHelper.OnSearch {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private SearchAdapter adapter;

    public static ExploreResultsFragment newInstance(String query) {
        Bundle arguments = new Bundle();
        arguments.putString("Query", query);
        ExploreResultsFragment exploreFragment = new ExploreResultsFragment();
        exploreFragment.setArguments(arguments);
        return exploreFragment;
    }

    public ExploreResultsFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
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
        actionBarTitleSetter.set("Search Results");
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
        new ItunesApiHelper(this).search(searchTerm);
    }

    @Override
    public void onSearch(List<Result> results) {
        updateAdapter(results);
    }

    @Override
    public void onError(Exception e) {
        Toast.makeText(getActivity(), "oopsies... search dun goofed", Toast.LENGTH_SHORT).show();
    }

    private void updateAdapter(List<Result> results) {
        adapter.updateData(results);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long itemId) {
        Result item = adapter.getItem(position);
        addFeedToFang(item);
    }

    private void addFeedToFang(Result item) {
        Toast.makeText(getActivity(), "Adding : " + item.getChannelTitle(), Toast.LENGTH_SHORT).show();
        ParseHelper parseHelper = new ParseHelper(getActivity().getContentResolver(), null);
        parseHelper.parse(getActivity(), item.getFeedUrl());
    }

}
