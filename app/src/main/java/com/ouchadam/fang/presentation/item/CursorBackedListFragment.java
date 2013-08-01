package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.ouchadam.fang.persistance.DataUpdater;
import com.ouchadam.fang.persistance.Query;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;

public abstract class CursorBackedListFragment<T> extends Fragment implements DataUpdater.DataUpdatedListener<T> {

    private TypedListAdapter<T> adapter;
    private DataQueryer<T> dataQueryer;
    private OnItemClickListener<T> onItemClickListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataQueryer = new DataQueryer<T>(activity, getQueryValues(), getMarshaller(), getLoaderManager(), this);
    }

    protected abstract Query getQueryValues();

    protected abstract CursorMarshaller<T> getMarshaller();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AbsListView root = getRootLayout(inflater, container);
        adapter = getAdapter();
        root.setAdapter(adapter);
        root.setOnItemClickListener(innerItemClickListener);
        return root;
    }

    private final AdapterView.OnItemClickListener innerItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(adapter, position);
            }
        }
    };

    protected void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    protected abstract AbsListView getRootLayout(LayoutInflater inflater, ViewGroup container);

    protected abstract TypedListAdapter<T> getAdapter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataQueryer.queryForData();
    }

    @Override
    public void onDataUpdated(List<T> data) {
        updateAdapter(data);
    }

    private void updateAdapter(List<T> data) {
        adapter.updateData(data);
    }

}