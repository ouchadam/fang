package com.ouchadam.fang.audio;

class ServiceLocation {

    private int bindCount;

    ServiceLocation() {
        this.bindCount = 0;
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

    public int get() {
        return bindCount;
    }

}
