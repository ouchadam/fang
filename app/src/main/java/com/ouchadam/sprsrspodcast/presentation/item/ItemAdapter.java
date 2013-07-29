package com.ouchadam.sprsrspodcast.presentation.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.novoda.notils.android.Views;
import com.ouchadam.sprsrspodcast.R;
import com.ouchadam.sprsrspodcast.domain.item.Item;

public class ItemAdapter extends TypedListAdapter<Item> {

    private final LayoutInflater layoutInflater;

    public ItemAdapter(LayoutInflater layoutInflater) {
        this.layoutInflater = layoutInflater;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        return getItemView(position, view, viewGroup);
    }

    private View getItemView(int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = createAdapterView(layoutInflater, position, viewGroup);
        }

        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Item item = getItem(position);

        updateViewPosition(viewHolder, position);
        updateViewHolder(viewHolder, item);
        return view;
    }

    private View createAdapterView(LayoutInflater inflater, int position, ViewGroup parent) {
        View view = inflater.inflate(R.layout.item_adapter, parent, false);
        initView(view, position);
        return view;
    }

    private void initView(View view, int position) {
        view.setTag(createViewHolder(view, position));
    }

    private ViewHolder createViewHolder(View view, int position) {
        ViewHolder holder = new ViewHolder();
        holder.title = Views.findById(view, R.id.text);
        holder.position = position;
        return holder;
    }

    private void updateViewPosition(ViewHolder viewHolder, int position) {
        viewHolder.position = position;
    }

    private void updateViewHolder(ViewHolder holder, Item item) {
        setHolderText(holder, item);
    }

    private void setHolderText(ViewHolder holder, Item item) {
        holder.title.setText(item.getTitle());
    }

    static class ViewHolder {
        TextView title;
        int position;
    }

}
