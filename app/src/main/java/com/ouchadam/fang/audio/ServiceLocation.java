package com.ouchadam.fang.audio;

class ServiceLocation {

    private boolean withinApp;

    ServiceLocation() {
        this.withinApp = false;
    }

    public void setWithinApp() {
        this.withinApp = true;
    }

    public void leavingApp() {
        this.withinApp = false;
    }

    public boolean isWithinApp() {
        return withinApp;
    }

}
