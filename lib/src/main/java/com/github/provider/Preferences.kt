/*
 * Copyright 2016, Moshe Waisberg
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
package com.github.provider

import android.content.Context
import android.net.Uri
import java.io.UnsupportedEncodingException
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.Charset
import timber.log.Timber

/**
 * Preferences provider columns.
 *
 * @author Moshe Waisberg
 */
object Preferences {
    @JvmStatic
    fun AUTHORITY(context: Context): String {
        return context.packageName + ".preferences"
    }

    @JvmStatic
    fun CONTENT_URI(context: Context): Uri {
        return Uri.parse("content://" + AUTHORITY(context))
    }

    /**
     * The MIME type of [.CONTENT_URI] providing a directory of preferences.
     */
    const val CONTENT_TYPE = "vnd.android.cursor.dir/com.github.preferences"

    /**
     * The MIME type of a [.CONTENT_URI] sub-directory of a single locale.
     */
    const val CONTENT_ITEM_TYPE = "vnd.android.cursor.item/com.github.preference"

    /**
     * All of the preferences.
     */
    const val ALL = "all"

    /**
     * The preference is a boolean.
     */
    const val BOOLEAN = "boolean"

    /**
     * The preference is a float.
     */
    const val FLOAT = "float"

    /**
     * The preference is an integer.
     */
    const val INT = "int"

    /**
     * The preference is a long.
     */
    const val LONG = "long"

    /**
     * The preference is a string.
     */
    const val STRING = "string"

    /**
     * The preference is a set of strings.
     */
    const val STRING_SET = "stringSet"

    /**
     * Encode the set of strings.
     *
     * @param set the set of strings.
     * @return the line-feed separated list of strings.
     */
    @JvmStatic
    fun fromStringSet(set: Set<String>?): String? {
        if (set.isNullOrEmpty()) {
            return null
        }
        try {
            val value = StringBuilder()
            val charsetName = Charset.defaultCharset().name()
            for ((i, s) in set.withIndex()) {
                if (i > 0) {
                    value.append('\n')
                }
                value.append(URLEncoder.encode(s, charsetName))
            }
            return value.toString()
        } catch (e: UnsupportedEncodingException) {
            // Shouldn't happen - UTF-8 always supported.
            Timber.e(e)
        }
        return null
    }

    /**
     * Decode a set of strings.
     *
     * @param encoded the line-feed separated list of strings.
     * @return the set of strings.
     */
    @JvmStatic
    fun toStringSet(encoded: String?): Set<String>? {
        if (encoded.isNullOrEmpty()) {
            return null
        }
        val set: MutableSet<String> = LinkedHashSet()
        val charsetName = Charset.defaultCharset().name()
        val tokens = encoded.split("\n".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        try {
            for (s in tokens) {
                set.add(URLDecoder.decode(s, charsetName))
            }
        } catch (e: UnsupportedEncodingException) {
            // Shouldn't happen - UTF-8 always supported.
            Timber.e(e)
        }
        return set
    }

    object Columns {
        /**
         * The preference key column.
         *
         * Type: TEXT
         */
        const val KEY = "key"

        /**
         * The preference value column.
         *
         * Type: OBJECT
         */
        const val VALUE = "value"

        /**
         * The preference type column.
         *
         * Type: TEXT
         */
        const val TYPE = "type"
    }
}