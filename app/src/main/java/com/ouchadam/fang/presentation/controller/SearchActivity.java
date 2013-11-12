package com.ouchadam.fang.presentation.controller;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import com.ouchadam.fang.R;
import com.ouchadam.fang.presentation.search.ExploreResultsFragment;

public class SearchActivity extends SecondLevelFangActivity {

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        onSearch(intent);
        setIntent(intent);
    }

    @Override
    protected void onFangCreate(Bundle savedInstanceState) {
        super.onFangCreate(savedInstanceState);
        onSearch(getIntent());
    }

    private void onSearch(Intent intent) {
        String query = getSearchQuery(intent);
        if (validate(query)) {
            handleSearch(query);
        }
    }

    private boolean validate(String query) {
        return !query.isEmpty();
    }

    private void handleSearch(String query) {
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, ExploreResultsFragment.newInstance(query)).commit();
    }

    private String getSearchQuery(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            return intent.getStringExtra(SearchManager.QUERY);
        }
        return "";
    }

}
