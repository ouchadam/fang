package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.novoda.notils.caster.Views;
import com.novoda.notils.java.Collections;
import com.ouchadam.fang.Log;
import com.ouchadam.fang.R;
import com.ouchadam.fang.debug.FeedServiceInfo;
import com.ouchadam.fang.persistance.DataUpdater;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.sync.FangSyncHelper;
import com.ouchadam.fang.sync.PullToRefreshExposer;

import java.util.List;

import novoda.android.typewriter.cursor.CursorMarshaller;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

public abstract class CursorBackedListFragment<T> extends Fragment implements DataUpdater.DataUpdatedListener<T>, OnRefreshListener, PullToRefreshExposer {

    private TypedListAdapter<T> adapter;
    private DataQueryer<T> dataQueryer;
    private OnItemClickListener<T> onItemClickListener;
    private OnLongClickListener<T> onLongClickListener;
    private AbsListView listView;
    private PullToRefreshLayout pullToRefreshLayout;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dataQueryer = new DataQueryer<T>(activity, getQueryValues(), getMarshaller(), getLoaderManager(), this);
    }

    protected abstract Query getQueryValues();

    protected abstract CursorMarshaller<T> getMarshaller();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = getRootLayout(inflater, container);
        adapter = createAdapter();
        listView = Views.findById(root, R.id.list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(innerItemClickListener);
        listView.setOnItemLongClickListener(innerItemLongClickListener);
        disallowChecking();

        onCreateViewExtra(root);
        return root;
    }

    @Override
    public void setRefreshing() {
        Log.e("!!!Set refreshing");
        pullToRefreshLayout.setRefreshing(true);
    }

    @Override
    public void refreshComplete() {
        if (pullToRefreshLayout.isRefreshing()) {
            Log.e("!!!Refreshing complete");
            pullToRefreshLayout.setRefreshComplete();
        }
    }

    protected void onCreateViewExtra(View root) {
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        pullToRefreshLayout = new PullToRefreshLayout(view.getContext());
        pullToRefreshLayout.setEnabled(true);
        ActionBarPullToRefresh.from(getActivity())
                .insertLayoutInto((ViewGroup) view)
                .theseChildrenArePullable(R.id.list)
                .listener(this)
                .setup(pullToRefreshLayout);
    }

    private final AdapterView.OnItemClickListener innerItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long itemId) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(adapter, position, itemId);
            }
        }
    };

    private final AdapterView.OnItemLongClickListener innerItemLongClickListener = new AdapterView.OnItemLongClickListener() {

        @Override
        public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long itemId) {
            if (onLongClickListener != null) {
                return onLongClickListener.onItemLongClick(adapter, view, position, itemId);
            }
            return false;
        }
    };

    protected void setOnItemClickListener(OnItemClickListener<T> onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnLongClickListener<T> {
        boolean onItemLongClick(TypedListAdapter<T> adapterView, View view, int position, long itemId);
    }

    protected void setOnItemLongClickListener(OnLongClickListener<T> onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }

    protected abstract View getRootLayout(LayoutInflater inflater, ViewGroup container);

    protected abstract TypedListAdapter<T> createAdapter();

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dataQueryer.queryForData();
        initPullToRefresh();
    }

    private void initPullToRefresh() {
        pullToRefreshLayout.setEnabled(canRefresh());
    }

    @Override
    public void onDataUpdated(List<T> data) {
        updateAdapter(data);
    }

    private void updateAdapter(List<T> data) {
        adapter.updateData(data);
    }

    protected TypedListAdapter<T> getAdapter() {
        return adapter;
    }

    protected void setLongPressChecked(int position) {
        listView.setItemChecked(position, true);
    }

    protected void allowChecking() {
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
    }

    protected void disallowChecking() {
        listView.post(new Runnable() {
            @Override
            public void run() {
                listView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
            }
        });
    }

    protected List<T> getAllCheckedPositions() {
        SparseBooleanArray rawCheckedItems = listView.getCheckedItemPositions();
        List<T> checkedItems = Collections.newArrayList();
        if (rawCheckedItems != null) {
            for (int index = 0; index < rawCheckedItems.size(); index++) {
                int position = rawCheckedItems.keyAt(index);
                boolean checked = rawCheckedItems.valueAt(index);
                if (checked) {
                    checkedItems.add(adapter.getItem(position));
                }
            }
        }
        return checkedItems;
    }

    protected void deselectAll() {
        for (int index = 0; index < listView.getCount(); index++) {
            listView.setItemChecked(index, false);
        }
    }

    @Override
    public void onRefreshStarted(View view) {
        FangSyncHelper.forceRefresh(FeedServiceInfo.Type.REFRESH);
    }

    protected boolean canRefresh() {
        return true;
    }

    public AbsListView getList() {
        return listView;
    }

}
