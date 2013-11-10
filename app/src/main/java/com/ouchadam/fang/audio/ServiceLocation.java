package com.ouchadam.fang.audio;

class ServiceLocation {

    private int bindCount;

    ServiceLocation() {
    }

    public void binding() {
        bindCount ++;
    }

    public void unbinding() {
        bindCount --;
    }

    public boolean isWithinApp() {
        return bindCount > 0;
    }

}
