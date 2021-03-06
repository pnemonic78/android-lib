/*
 * Copyright 2012, Moshe Waisberg
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.util;

import timber.log.Timber;

/**
 * Logger utilities.
 *
 * @author Moshe Waisberg
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
