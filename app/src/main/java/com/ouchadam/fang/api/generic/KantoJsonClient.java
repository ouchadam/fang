package com.ouchadam.fang.api.generic;

import com.google.api.client.googleapis.GoogleUtils;
import com.google.api.client.googleapis.services.AbstractGoogleClientRequest;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.Preconditions;

import java.io.IOException;


public class KantoJsonClient extends AbstractGoogleJsonClient {

    static {
        Preconditions.checkState(GoogleUtils.MAJOR_VERSION == 1 && GoogleUtils.MINOR_VERSION >= 15,
                "You are currently running with version %s of google-api-client. " +
                        "You need at least version 1.15 of google-api-client to run version " +
                        "1.17.0-rc of the helloworld library.", GoogleUtils.VERSION);
    }

    public static class Builder extends AbstractGoogleJsonClient.Builder {

        public Builder(HttpTransport httpTransport, JsonFactory jsonFactory, String defaultRootUrl, String defaultServicePath, HttpRequestInitializer requestInitializer) {
            super(httpTransport, jsonFactory, defaultRootUrl, defaultServicePath, requestInitializer, false);
        }

        @Override
        public KantoJsonClient build() {
            return new KantoJsonClient(this);
        }

        @Override
        public Builder setApplicationName(String applicationName) {
            return (Builder) super.setApplicationName(applicationName);
        }

    }

    private KantoJsonClient(Builder builder) {
        super(builder);
    }

    @Override
    public void initialize(AbstractGoogleClientRequest<?> httpClientRequest) throws IOException {
        super.initialize(httpClientRequest);
    }

}
