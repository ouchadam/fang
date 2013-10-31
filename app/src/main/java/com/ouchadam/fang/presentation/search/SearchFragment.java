package com.ouchadam.fang.presentation.search;

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
import com.ouchadam.fang.api.search.ItunesSearch;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.api.search.SearchResult;
import com.ouchadam.fang.debug.ParseHelper;
import com.ouchadam.fang.domain.channel.Channel;

import java.util.List;

public class SearchFragment extends Fragment implements AdapterView.OnItemClickListener, ParseHelper.OnParseFinishedListener {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    private ListView listView;
    private SearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        listView = Views.findById(root, R.id.list);
        adapter = new SearchAdapter(LayoutInflater.from(getActivity()), getActivity());
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        searchFor("bbc");
    }

    private void searchFor(final String searchTerm) {
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
                final SearchResult search = new ItunesSearch().search(searchTerm);
                HANDLER.post(new Runnable() {
                    @Override
                    public void run() {
                        updateAdapter(search.getResults());
                    }
                });
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
        parseHelper.parse(item.getFeedUrl());
    }

    @Override
    public void onParseFinished(Channel channel) {
        Toast.makeText(getActivity(), "Added : "  + channel.getTitle(), Toast.LENGTH_SHORT).show();
    }
}
