package com.ouchadam.fang.presentation.item;

import android.view.View;
import android.widget.TextView;

import com.novoda.notils.caster.Views;
import com.ouchadam.fang.FangCalendar;
import com.ouchadam.fang.R;

class LastUpdateController {

    private final LastUpdatedManager lastUpdatedManager;
    private final TextView lastUpdateText;
    private final View lastUpdatedContainer;

    static LastUpdateController from(View root) {
        LastUpdatedManager lastUpdatedManager = LastUpdatedManager.from(root.getContext());
        View lastUpdatedContainer = Views.findById(root, R.id.last_updated_container);
        TextView lastUpdateText = Views.findById(root, R.id.last_updated_text);
        View lastUpdatedClose = Views.findById(root, R.id.last_updated_close);
        return new LastUpdateController(lastUpdatedManager, lastUpdateText, lastUpdatedContainer, lastUpdatedClose);
    }

    LastUpdateController(LastUpdatedManager lastUpdatedManager, TextView lastUpdateText, View lastUpdatedContainer, View lastUpdatedClose) {
        this.lastUpdatedManager = lastUpdatedManager;
        this.lastUpdateText = lastUpdateText;
        this.lastUpdatedContainer = lastUpdatedContainer;
        lastUpdatedClose.setOnClickListener(onLastUpdatedClose);
    }

    private final View.OnClickListener onLastUpdatedClose = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            lastUpdatedManager.setSeen(true);
            lastUpdatedContainer.setVisibility(View.GONE);
        }
    };

    void handleLastUpdated() {
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

}
