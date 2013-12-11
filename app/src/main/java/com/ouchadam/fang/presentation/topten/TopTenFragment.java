package com.ouchadam.fang.presentation.topten;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.parsing.itunesrss.Entry;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastFeed;
import com.ouchadam.fang.parsing.itunesrss.TopPodcastParser;
import com.ouchadam.fang.presentation.search.TopTenAdapter;

import org.apache.http.protocol.HTTP;
import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

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
        listView.setOnItemClickListener(onItemClickListener);
        return root;
    }

    private final ListView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
            Entry entry = adapter.getItem(position);
            subscribeToEntry(entry);
        }
    };

    private void subscribeToEntry(Entry entry) {
        Toast.makeText(getActivity(), "Adding : " + entry.getName(), Toast.LENGTH_SHORT).show();
        new EntrySubscriber(entry, getActivity()).subscribe();
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
                topTen.forEach(forEach);
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
            Log.e("!!! : " + entry.getName() + " " + entry.getId());
        }
    };

    private TopPodcastFeed getTopTenFor(String url) {
        TopPodcastParser topPodcastParser = TopPodcastParser.newInstance();
        try {
            Log.e("!!! : url : " + url);
            InputSource source = new InputSource(new URL(url).openStream());
            source.setEncoding(HTTP.UTF_8);
            topPodcastParser.parse(source.getByteStream());
            return topPodcastParser.getResult();
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private void logUrl(InputStream urlInputStream) {
        Scanner s = new java.util.Scanner(urlInputStream).useDelimiter("\\A");
        Log.e(s.hasNext() ? s.next() : "");
    }

    private InputStream getInputStreamFrom(String url) throws IOException {
        URL urlForStream = new URL(url);
        return urlForStream.openStream();
    }
}
