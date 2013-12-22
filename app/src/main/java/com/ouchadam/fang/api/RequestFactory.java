package com.ouchadam.fang.api;

import com.ouchadam.kanto.api.generic.KantoJsonClient;
import com.ouchadam.kanto.api.generic.KantoRequest;
import com.ouchadam.kanto.api.generic.RequestBuilder;
import com.ouchadam.kanto.api.model.Feed;
import com.ouchadam.kanto.api.model.FeedList;

import java.io.IOException;

public class RequestFactory {

    private KantoJsonClient kantoJsonClient;

    public RequestFactory(KantoJsonClient kantoJsonClient) {
        this.kantoJsonClient = kantoJsonClient;
    }

    public KantoRequest<FeedList> listFeeds() throws IOException {
        KantoRequest<FeedList> request = new RequestBuilder<Void, FeedList>()
                .withClient(kantoJsonClient)
                .withParseType(FeedList.class)
                .withRestPath("list")
                .withHttpMethod(RequestBuilder.HttpMethod.GET)
                .build();

        initialiseRequest(request);
        return request;
    }

    private KantoRequest<FeedList> initialiseRequest(KantoRequest<FeedList> request) {
        return request;
    }

    public KantoRequest<FeedList> addFeed(Feed feed) throws IOException {
        KantoRequest<FeedList> request = new RequestBuilder<Feed, FeedList>()
                .withClient(kantoJsonClient)
                .withContent(feed)
                .withParseType(FeedList.class)
                .withRestPath("add")
                .withHttpMethod(RequestBuilder.HttpMethod.POST)
                .build();

        kantoJsonClient.initialize(request);
        return request;
    }

}
