package com.ouchadam.fang.api.auth;

class MissingAccountException extends RuntimeException {
    public MissingAccountException() {
        super("Failed to find any accounts");
    }
}
