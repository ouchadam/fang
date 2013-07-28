package com.ouchadam.sprsrspodcast.persistance.database;

public interface Executable<R, P> {
    R execute(P what) throws ExecutionFailure;

    class ExecutionFailure extends Exception {
        public ExecutionFailure(Exception wrapper) {
            super(wrapper);
        }
    }
}