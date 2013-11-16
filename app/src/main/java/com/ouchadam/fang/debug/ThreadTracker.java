package com.ouchadam.fang.debug;

class ThreadTracker {

    private final OnAllThreadsComplete onAllThreadsComplete;
    private int threadCount;

    interface OnAllThreadsComplete {
        void onFinish();
    }

    public ThreadTracker(int threadCount, OnAllThreadsComplete onAllThreadsComplete) {
        this.threadCount = threadCount;
        this.onAllThreadsComplete = onAllThreadsComplete;
    }

    public synchronized void threadFinished() {
        if (--threadCount == 0) {
            onAllThreadsComplete.onFinish();
        }
    }

}
