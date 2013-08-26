package com.ouchadam.fang.presentation.search;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.novoda.notils.android.Views;
import com.ouchadam.fang.R;
import com.ouchadam.fang.api.search.ItunesSearch;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.api.search.SearchResult;

import java.util.List;

public class SearchFragment extends Fragment {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());

    private ListView listView;
    private SearchAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);

        listView = Views.findById(root, R.id.list);
        adapter = new SearchAdapter(LayoutInflater.from(getActivity()), getActivity());
        listView.setAdapter(adapter);

        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SearchResult search = new ItunesSearch().search("bbc");
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

}
