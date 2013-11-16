package com.ouchadam.fang.debug;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

class ThreadExecutor {

    private final static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 15, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(25));

    public void run(Runnable runnable) {
        threadPool.execute(runnable);
    }

}
