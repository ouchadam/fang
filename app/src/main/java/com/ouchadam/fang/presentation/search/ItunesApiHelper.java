package com.ouchadam.fang.presentation.search;

import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.ouchadam.fang.api.search.ItunesSearch;
import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.api.search.SearchResult;

import java.util.List;

public class ItunesApiHelper {

    private final static Handler HANDLER = new Handler(Looper.getMainLooper());
    private final OnSearch onSearch;

    public interface OnSearch {

        void onSearch(List<Result> results);
        void onError(Exception e);
    }
    public ItunesApiHelper(OnSearch onSearch) {
        this.onSearch = onSearch;
    }

    // TODO give this horrible quick hack some love
    public void search(final String term) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SearchResult search;
                try {
                    search = new ItunesSearch().search(term);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onSearch.onSearch(search.getResults());
                        }
                    });
                } catch (final ItunesSearch.ItunesSearchException e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onSearch.onError(e);
                        }
                    });
                }

            }
        }).start();
    }

    public void lookup(final String id) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SearchResult search;
                try {
                    search = new ItunesSearch().lookup(id);
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onSearch.onSearch(search.getResults());
                        }
                    });
                } catch (final ItunesSearch.ItunesSearchException e) {
                    HANDLER.post(new Runnable() {
                        @Override
                        public void run() {
                            onSearch.onError(e);
                        }
                    });
                }

            }
        }).start();

    }

}
