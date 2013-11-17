package com.ouchadam.fang;

public class Log {

    public static final boolean SHOW_LOGS = true;
    private static final String TAG = "Fang Debug";

    private Log() {
    }

    public static void i(String msg) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.i(TAG, getDetailedLog(msg));
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void d(String msg) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.d(TAG, getDetailedLog(msg));
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void w(String msg) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.w(TAG, getDetailedLog(msg));
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void e(String msg) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.e(TAG, getDetailedLog(msg));
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void w(String msg, Throwable t) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.w(TAG, getDetailedLog(msg), t);
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void d(String msg, Throwable t) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.d(TAG, getDetailedLog(msg), t);
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void e(String msg, Throwable t) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.e(TAG, getDetailedLog(msg), t);
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    public static void wtf(String msg, Throwable t) {
        try {
            if (SHOW_LOGS) {
                android.util.Log.wtf(TAG, getDetailedLog(msg), t);
            }
        } catch (RuntimeException ignore) {
            logError(ignore);
        }
    }

    private static String getDetailedLog(String msg) {
        return msg;
    }

    private static void logError(Throwable ignore) {
        android.util.Log.e(TAG, "Error", ignore);
    }

}
