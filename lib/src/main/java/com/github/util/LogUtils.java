package com.github.util;

import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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

    public static class LogTree extends Timber.DebugTree {

        private final boolean release;

        public LogTree(boolean debug) {
            this.release = !debug;
        }

        @Override
        protected void log(int priority, @Nullable String tag, @NotNull String message, @Nullable Throwable t) {
            if (release && ((priority == Log.DEBUG) || (priority == Log.VERBOSE))) {
                return;
            }
            super.log(priority, tag, message, t);
        }
    }
}
