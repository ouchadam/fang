package com.ouchadam.fang.debug;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

class ServiceInfo {

    public static final String TYPE = "type";
    public static final String URLS = "urls";

    public enum Type {
        REFRESH,
        ADD;
    }

    private final Bundle bundle;

    static ServiceInfo from(Bundle bundle) {
        return new ServiceInfo(bundle);
    }

    ServiceInfo(Bundle bundle) {
        this.bundle = bundle;
    }

    public Type getType() {
        return Type.valueOf(bundle.getString(TYPE));
    }

    public ArrayList<String> getUrlsToAdd() {
        return bundle.getStringArrayList(URLS);
    }

    static Intent refresh(Context context) {
        Intent intent = new Intent(context, ChannelFeedDownloadService.class);
        intent.putExtra(TYPE, Type.REFRESH.name());
        return intent;
    }

    static Intent add(Context context, String... url) {
        Intent intent = new Intent(context, ChannelFeedDownloadService.class);
        intent.putExtra(TYPE, Type.ADD.name());
        ArrayList<String> urlsAsList = new ArrayList<String>(Arrays.asList(url));
        intent.putStringArrayListExtra(URLS, urlsAsList);
        return intent;
    }

}
