package com.ouchadam.fang.api.generic;

import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClient;
import com.google.api.client.googleapis.services.json.AbstractGoogleJsonClientRequest;
import com.google.api.client.util.Key;

public abstract class KantoRequest<T> extends AbstractGoogleJsonClientRequest<T> {

  public KantoRequest(AbstractGoogleJsonClient client, String method, String uriTemplate, Object content, Class<T> responseClass) {
    super(client, method, uriTemplate, content, responseClass);
  }

  @Key
  private String fields;

  public String getFields() {
    return fields;
  }

  public KantoRequest<T> setFields(String fields) {
    this.fields = fields;
    return this;
  }

  @Key
  private String key;

  public String getKey() {
    return key;
  }

  public KantoRequest<T> setKey(String key) {
    this.key = key;
    return this;
  }

  @Key("oauth_token")
  private String oauthToken;

  public String getOauthToken() {
    return oauthToken;
  }

  public KantoRequest<T> setOauthToken(String oauthToken) {
    this.oauthToken = oauthToken;
    return this;
  }

  @Key
  private Boolean prettyPrint;

  public Boolean getPrettyPrint() {
    return prettyPrint;
  }

  public KantoRequest<T> setPrettyPrint(Boolean prettyPrint) {
    this.prettyPrint = prettyPrint;
    return this;
  }

}
