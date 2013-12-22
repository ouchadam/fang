package com.ouchadam.fang.api;

import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.extensions.android.json.AndroidJsonFactory;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.ouchadam.kanto.api.generic.KantoJsonClient;

import javax.annotation.Nullable;

public class Constants {

    private static final String WEB_CLIENT_ID = "511891654233-tam3inuii05jlbn5acpp5r7r9384dk52.apps.googleusercontent.com";
    public static final String AUDIENCE = "server:client_id:" + WEB_CLIENT_ID;
    private static final String DEFAULT_ROOT_URL = "https://fang-kanto.appspot.com/_ah/api/";

    private static final String VERSION = "v1";
    private static final String REST_NAME = "endpoint";
    private static final String DEFAULT_SERVICE_PATH = REST_NAME + "/" + VERSION + "/";
    private static final String DEFAULT_BASE_URL = DEFAULT_ROOT_URL + DEFAULT_SERVICE_PATH;

    private static final String APPLICATION_NAME = "fang-kanto";

    private static final JsonFactory JSON_FACTORY = new AndroidJsonFactory();
    private static final HttpTransport HTTP_TRANSPORT = AndroidHttp.newCompatibleTransport();

    private static final String DEV_SERVER_ADDRESS = "http://192.168.0.4:8080";

    public static KantoJsonClient getApiServiceHandle(@Nullable GoogleAccountCredential credential) {
        return (KantoJsonClient) new KantoJsonClient.Builder(Constants.HTTP_TRANSPORT, Constants.JSON_FACTORY, DEFAULT_ROOT_URL, DEFAULT_SERVICE_PATH, credential)
                .setApplicationName(APPLICATION_NAME)
                .setRootUrl(DEV_SERVER_ADDRESS + "/_ah/api/") /* point to your own dev server or not at all */
                .build();
    }


}
