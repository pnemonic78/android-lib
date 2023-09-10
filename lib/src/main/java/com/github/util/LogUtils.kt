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
package com.github.util

import timber.log.Timber

/**
 * Logger utilities.
 *
 * @author Moshe Waisberg
 */
object LogUtils {
    @JvmStatic
    fun e(tag: String, msg: String?, e: Throwable?) {
        Timber.e(e, msg)
    }

    @JvmStatic
    fun e(tag: String, e: Throwable?) {
        Timber.e(e)
    }

    @JvmStatic
    fun e(tag: String, msg: String?) {
        Timber.e(msg)
    }

    @JvmStatic
    fun i(tag: String, msg: String?) {
        Timber.i(msg)
    }

    @JvmStatic
    fun v(tag: String, msg: String?) {
        Timber.v(msg)
    }

    @JvmStatic
    fun w(tag: String, msg: String?) {
        Timber.w(msg)
    }
}