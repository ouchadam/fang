package com.ouchadam.fang.api.generic;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;

public class RequestBuilder<F, T> {

    private AbstractGoogleJsonClient client;
    private String restPath;
    private F content;
    private Class<T> type;
    private HttpMethod httpMethod;

    public enum HttpMethod {
        POST {
            @Override
            public String get() {
                return "POST";
            }
        },
        GET {
            @Override
            public String get() {
                return "GET";
            }
        };

        public abstract String get();
    }

    public RequestBuilder<F, T> withClient(AbstractGoogleJsonClient client) {
        this.client = client;
        return this;
    }

    public RequestBuilder<F, T> withRestPath(String restPath) {
        this.restPath = restPath;
        return this;
    }

    public RequestBuilder<F, T> withContent(F content) {
        this.content = content;
        return this;
    }

    public RequestBuilder<F, T> withParseType(Class<T> type) {
        this.type = type;
        return this;
    }

    public RequestBuilder<F, T> withHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
        return this;
    }

    public KantoRequest<T> build() {
        return new StubRequest<F, T>(client, httpMethod, restPath, content, type);
    }

    private static class StubRequest<F, T> extends KantoRequest<T> {
        protected StubRequest(AbstractGoogleJsonClient client, HttpMethod httpMethod, String restPath, F content, Class<T> parseTo) {
            super(client, httpMethod.get(), restPath, content, parseTo);
        }
    }

}
