package com.ouchadam.fang.api.auth;

public interface AuthResult {
    void onSuccess(String emailAccount);
    void onError();
}
