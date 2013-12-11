package com.ouchadam.fang.presentation.topten;

import android.content.Context;

import com.ouchadam.fang.api.search.Result;
import com.ouchadam.fang.debug.ParseHelper;
import com.ouchadam.fang.parsing.itunesrss.Entry;
import com.ouchadam.fang.presentation.search.ItunesApiHelper;

import java.util.List;

class EntrySubscriber implements ItunesApiHelper.OnSearch {

    private final Entry entry;
    private final Context context;

    public EntrySubscriber(Entry entry, Context context) {
        this.entry = entry;
        this.context = context;
    }

    public void subscribe() {
        new ItunesApiHelper(this).lookup(entry.getId());
    }

    @Override
    public void onSearch(List<Result> results) {
        Result result = results.get(0);
        ParseHelper parseHelper = new ParseHelper(context.getContentResolver(), null);
        parseHelper.parse(context, result.getFeedUrl());
    }

    @Override
    public void onError(Exception e) {

    }

}
