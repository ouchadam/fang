package com.ouchadam.fang.api.auth;

public class MissingGooglePlayServicesException extends Exception {

    private final int connectionStatusCode;

    public MissingGooglePlayServicesException(int connectionStatusCode) {
        this.connectionStatusCode = connectionStatusCode;
    }

    public int getConnectionStatusCode() {
        return connectionStatusCode;
    }
}
