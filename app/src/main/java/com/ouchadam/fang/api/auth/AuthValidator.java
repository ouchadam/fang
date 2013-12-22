package com.ouchadam.fang.api.auth;

public interface AuthValidator {
    void validateAuthOrThrow() throws AuthenticationException;
}
