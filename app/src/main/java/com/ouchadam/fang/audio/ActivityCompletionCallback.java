package com.ouchadam.fang.audio;

public class ActivityCompletionCallback  {

    private CompletionListener onCompletionListener;

    public void setActivityListener(CompletionListener onCompletionListener) {
        this.onCompletionListener = onCompletionListener;
    }

    public void onComplete() {
        this.onCompletionListener.onComplete();
    }

    public void removeListener() {
        this.onCompletionListener = null;
    }
}
