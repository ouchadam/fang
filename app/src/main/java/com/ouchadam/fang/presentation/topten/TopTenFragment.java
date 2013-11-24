package com.ouchadam.fang.presentation.topten;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.parsing.itunesrss.Entry;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastFeed;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastParser;
import com.ouchadam.fang.presentation.search.TopTenAdapter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class TopTenFragment extends Fragment {

    private static final String ARG_TYPE = "ARG_TYPE";

    private ListView listView;
    private TopTenAdapter adapter;

    public static TopTenFragment newInstance(TopTenType topTenType) {
        TopTenFragment topTenFragment = new TopTenFragment();
        Bundle arguments = new Bundle();
        arguments.putString(ARG_TYPE, topTenType.name());
        topTenFragment.setArguments(arguments);
        return topTenFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_item_list, container, false);
        listView = Views.findById(root, R.id.list);
        adapter = new TopTenAdapter(inflater, getActivity());
        listView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getTopTen(getType());
    }

    private TopTenType getType() {
        return TopTenType.valueOf(getArguments().getString(ARG_TYPE));
    }

    private void getTopTen(final TopTenType topTenType) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final TopPodcastFeed topTen = getTopTenFor(topTenType.getUrl());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.updateData(topTen.getEntries());
                    }
                });


            }
        }).start();
    }

    private final TopPodcastFeed.ForEach forEach = new TopPodcastFeed.ForEach() {
        @Override
        public void onEach(Entry entry) {
            Log.e("!!! : " + entry.getName());
        }
    };

    private TopPodcastFeed getTopTenFor(String url) {
        TopPodcastParser topPodcastParser = TopPodcastParser.newInstance();
        try {
            InputStream urlInputStream = getInputStreamFrom(url);
            topPodcastParser.parse(urlInputStream);
            return topPodcastParser.getResult();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private InputStream getInputStreamFrom(String url) throws IOException {
        URL urlForStream = new URL(url);
        return urlForStream.openStream();
    }
}
