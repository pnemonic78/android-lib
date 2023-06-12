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
package com.github.content

import android.annotation.TargetApi
import android.content.Intent
import android.os.Build
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

/**
 * Intent utilities.
 *
 * @author Moshe Waisberg
 */
object IntentUtils {
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun putExtras(src: Intent, dest: android.os.PersistableBundle) {
        val extras = src.extras
        if (extras == null || extras.isEmpty) {
            return
        }
        var value: Any?
        for (key in extras.keySet()) {
            value = extras[key]
            when (value) {
                null -> dest.putString(key, null)
                is Boolean? -> dest.putBoolean(key, value)
                is Boolean -> dest.putBoolean(key, value)
                is CharSequence? -> dest.putString(key, value.toString())
                is Double? -> dest.putDouble(key, value)
                is Double -> dest.putDouble(key, value)
                is Float? -> dest.putDouble(key, value.toDouble())
                is Float -> dest.putDouble(key, value.toDouble())
                is Int? -> dest.putInt(key, value)
                is Int -> dest.putInt(key, value)
                is Long? -> dest.putLong(key, value)
                is Long -> dest.putLong(key, value)
                is AtomicInteger -> dest.putInt(key, value.get())
                is AtomicLong -> dest.putLong(key, value.get())
                is Number? -> dest.putDouble(key, value.toDouble())
                is BooleanArray? -> dest.putBooleanArray(key, value)
                is DoubleArray? -> dest.putDoubleArray(key, value)
                is IntArray? -> dest.putIntArray(key, value)
                is LongArray? -> dest.putLongArray(key, value)
                //is Array<String> -> dest.putStringArray(key, value)
            }
        }
    }

    @JvmStatic
    @TargetApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    fun readExtras(dest: Intent, src: android.os.PersistableBundle?) {
        if (src == null || src.isEmpty) {
            return
        }
        var value: Any?
        for (key in src.keySet()) {
            value = src[key]
            when (value) {
                null -> dest.putExtra(key, null as String?)
                is Boolean? -> dest.putExtra(key, value)
                is Boolean -> dest.putExtra(key, value)
                is CharSequence? -> dest.putExtra(key, value)
                is Double? -> dest.putExtra(key, value)
                is Double -> dest.putExtra(key, value)
                is Float? -> dest.putExtra(key, value)
                is Float -> dest.putExtra(key, value)
                is Int? -> dest.putExtra(key, value)
                is Int -> dest.putExtra(key, value)
                is Long? -> dest.putExtra(key, value)
                is Long -> dest.putExtra(key, value)
                is AtomicInteger -> dest.putExtra(key, value.get())
                is AtomicLong -> dest.putExtra(key, value.get())
                is Number? -> dest.putExtra(key, value)
                is BooleanArray? -> dest.putExtra(key, value)
                is DoubleArray? -> dest.putExtra(key, value)
                is IntArray? -> dest.putExtra(key, value)
                is LongArray? -> dest.putExtra(key, value)
                //is Array<String> -> dest.putExtra(key, value)
            }
        }
    }
}