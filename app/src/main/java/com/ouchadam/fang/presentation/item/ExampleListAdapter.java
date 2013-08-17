package com.ouchadam.fang.presentation.item;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ouchadam.bookkeeper.domain.ProgressValues;
import com.ouchadam.bookkeeper.watcher.ListItemWatcher;
import com.ouchadam.bookkeeper.watcher.adapter.ListItemProgress;
import com.ouchadam.bookkeeper.watcher.adapter.ProgressDelegate;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.item.Item;

public class ExampleListAdapter extends TypedListAdapter<Item> implements ListItemWatcher.ItemWatcher {

    private final LayoutInflater layoutInflater;
    private final ProgressDelegate<ViewHolder> progressDelegate;

    public ExampleListAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
        this.progressDelegate = new ItemProgressManager(this);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        View view = createView(convertView, viewGroup);
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        progressDelegate.handleDownloadProgress(position, viewHolder);
        ListItemProgress.Stage stage = progressDelegate.getStage(position);
        if (stage == ListItemProgress.Stage.IDLE) {
            viewHolder.textView.setText(getItem(position).getTitle());
        }

        Log.e("!!!", "has for : " + position + " : " + getItem(position).hashCode());

        return view;
    }

    private View createView(View convertView, ViewGroup viewGroup) {
        return convertView == null ? createNewView(viewGroup) : convertView;
    }

    private View createNewView(ViewGroup viewGroup) {
        View newView = layoutInflater.inflate(R.layout.downloadable_item_adapter, viewGroup, false);
        initViewTag(newView);
        return newView;
    }

    private void initViewTag(View newView) {
        ViewHolder tag = new ViewHolder();
        initHolder(newView, tag);
        newView.setTag(tag);
    }

    private void initHolder(View newView, ViewHolder tag) {
        tag.textView = (TextView) newView.findViewById(R.id.text);
        tag.progressBar = (ProgressBar) newView.findViewById(R.id.progress);
    }

    @Override
    public void setStageFor(long itemId, ListItemProgress.Stage stage) {
        progressDelegate.setStageFor(itemId, stage);
    }

    @Override
    public void updateProgressValuesFor(long itemId, ProgressValues progressValues) {
        progressDelegate.updateProgressValuesFor(itemId, progressValues);

    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

    public static class ViewHolder {
        TextView textView;
        ProgressBar progressBar;
    }

}