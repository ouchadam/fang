package com.ouchadam.fang.presentation.item;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.frankiesardo.icepick.bundle.Bundles;
import com.novoda.notils.caster.Classes;
import com.novoda.notils.caster.Views;
import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.R;
import com.ouchadam.fang.domain.FullItem;
import com.ouchadam.fang.persistance.FangProvider;
import com.ouchadam.fang.persistance.Query;
import com.ouchadam.fang.persistance.database.Tables;
import com.ouchadam.fang.persistance.database.Uris;
import com.ouchadam.fang.presentation.FullItemMarshaller;
import com.ouchadam.fang.presentation.panel.SlidingPanelExposer;

import novoda.android.typewriter.cursor.CursorMarshaller;

public class LatestFragment extends CursorBackedListFragment<FullItem> implements OnItemClickListener<FullItem> {

    private final ActionBarTitleSetter actionBarTitleSetter;
    private DetailsDisplayManager detailsDisplayManager;
    private View lastUpdatedContainer;
    private TextView lastUpdateText;
    private LastUpdatedManager lastUpdatedManager;

    public LatestFragment() {
        this.actionBarTitleSetter = new ActionBarTitleSetter();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        actionBarTitleSetter.onAttach(activity);
        SlidingPanelExposer panelController = Classes.from(activity);
        detailsDisplayManager = new DetailsDisplayManager(panelController, new NavigatorForResult(activity));
        lastUpdatedManager = LastUpdatedManager.from(activity);
    }

    @Override
    protected View getRootLayout(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_item_list, container, false);
    }

    @Override
    protected void onCreateViewExtra(View root) {
        super.onCreateViewExtra(root);
        lastUpdatedContainer = Views.findById(root, R.id.last_updated_container);
        lastUpdateText = Views.findById(root, R.id.last_updated_text);
        Views.findById(root, R.id.last_updated_close).setOnClickListener(onLastUpdatedClose);
    }

    private final View.OnClickListener onLastUpdatedClose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            lastUpdatedManager.setSeen(true);
            lastUpdatedContainer.setVisibility(View.GONE);
        }
    };

    @Override
    protected TypedListAdapter<FullItem> createAdapter() {
        return new ItemAdapter(LayoutInflater.from(getActivity()), getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundles.restoreInstanceState(this, savedInstanceState);
        actionBarTitleSetter.set("Latest");
        handleLastUpdated();
    }

    private void handleLastUpdated() {
        String lastUpdatedText = getLastUpdatedText();
        lastUpdateText.setText(lastUpdatedText);
        lastUpdatedContainer.setVisibility(shouldShowLastUpdated() ? View.VISIBLE : View.GONE);
    }

    private String getLastUpdatedText() {
        long lastUpdatedMs = lastUpdatedManager.getLastUpdatedMs();
        return "Last updated " + new FangCalendar(lastUpdatedMs).getTimeAgo();
    }

    private boolean shouldShowLastUpdated() {
        return lastUpdatedManager.canShow();
    }

    @Override
    protected Query getQueryValues() {
        return new Query.Builder()
                .withUri(FangProvider.getUri(Uris.FULL_ITEM))
                .withSorter(" CAST (" + Tables.Item.PUBDATE + " AS DECIMAL)" + " DESC")
                .build();
    }

    @Override
    protected CursorMarshaller<FullItem> getMarshaller() {
        return new FullItemMarshaller();
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(TypedListAdapter<FullItem> adapter, int position, long itemId) {
        detailsDisplayManager.displayItem(itemId);
    }

}
