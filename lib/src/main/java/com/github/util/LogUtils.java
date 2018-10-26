package com.github.util;

import timber.log.Timber;

/**
 * Logger utilities.
 */
public class LogUtils {

    public static void e(String tag, String msg, Throwable e) {
        Timber.e(e, msg);
    }

    public static void e(String tag, Throwable e) {
        Timber.e(e);
    }

    public static void e(String tag, String msg) {
        Timber.e(msg);
    }

    public static void i(String tag, String msg) {
        Timber.i(msg);
    }

    public static void v(String tag, String msg) {
        Timber.v(msg);
    }

    public static void w(String tag, String msg) {
        Timber.w(msg);
    }
}
