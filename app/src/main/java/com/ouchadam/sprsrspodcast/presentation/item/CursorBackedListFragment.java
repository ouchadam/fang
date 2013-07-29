package com.ouchadam.sprsrspodcast.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.ouchadam.sprsrspodcast.persistance.DataUpdater;
import com.ouchadam.sprsrspodcast.persistance.Query;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;

public abstract class CursorBackedListFragment<T> extends Fragment implements DataUpdater.DataUpdatedListener<T> {

    private DataUpdater<T> dataUpdater;
    private TypedListAdapter<T> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AbsListView root = getRootLayout(inflater, container);
        adapter = getAdapter();
        root.setAdapter(adapter);
        root.setOnItemClickListener(getItemClickListener());
        return root;
    }

    protected AdapterView.OnItemClickListener getItemClickListener() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        };
    }

    protected abstract AbsListView getRootLayout(LayoutInflater inflater, ViewGroup container);

    protected abstract TypedListAdapter<T> getAdapter();

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        queryForData();
    }

    private void queryForData() {
        if (dataUpdater != null) {
            dataUpdater.stopWatchingData();
        }
        dataUpdater = new DataUpdater<T>(getActivity(), getQueryValues(), getMarshaller(), this, getLoaderManager());
        dataUpdater.startWatchingData();
    }

    protected abstract Query getQueryValues();

    protected abstract CursorMarshaller<T> getMarshaller();

    @Override
    public void onDataUpdated(List<T> data) {
        updateAdapter(data);
    }

    private void updateAdapter(List<T> data) {
        adapter.updateData(data);
    }

}
