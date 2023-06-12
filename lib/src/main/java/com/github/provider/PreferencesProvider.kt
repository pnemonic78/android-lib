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

import android.content.ContentProvider
import android.content.ContentValues
import android.content.SharedPreferences
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import androidx.preference.PreferenceManager
import com.github.provider.Preferences.AUTHORITY
import com.github.provider.Preferences.fromStringSet
import com.github.provider.Preferences.toStringSet

/**
 * Content provider that wraps a real [android.content.SharedPreferences].
 *
 * @author Moshe Waisberg
 */
class PreferencesProvider : ContentProvider() {

    private var delegate: SharedPreferences? = null
    private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH)

    override fun onCreate(): Boolean {
        val context = context!!
        delegate = PreferenceManager.getDefaultSharedPreferences(context)
        val authority = AUTHORITY(context)
        uriMatcher.addURI(authority, "", ALL)
        uriMatcher.addURI(authority, "all", ALL)
        uriMatcher.addURI(authority, "all/*", ALL_KEY)
        uriMatcher.addURI(authority, "boolean/*", BOOLEAN)
        uriMatcher.addURI(authority, "float/*", FLOAT)
        uriMatcher.addURI(authority, "int/*", INT)
        uriMatcher.addURI(authority, "integer/*", INT)
        uriMatcher.addURI(authority, "long/*", LONG)
        uriMatcher.addURI(authority, "string/*", STRING)
        uriMatcher.addURI(authority, "stringSet/*", STRING_SET)
        uriMatcher.addURI(authority, "string_set/*", STRING_SET)
        uriMatcher.addURI(authority, "strings/*", STRING_SET)
        return true
    }

    override fun getType(uri: Uri): String {
        val match = uriMatcher.match(uri)
        return if (match == ALL) {
            Preferences.CONTENT_TYPE
        } else {
            Preferences.CONTENT_ITEM_TYPE
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val delegate = this.delegate ?: return null
        var key = uri.lastPathSegment!!
        val match = uriMatcher.match(uri)
        var value: Any?
        var type: String
        val types: MutableList<String>
        val keys: MutableList<String>
        val values: MutableList<Any?>
        var cursor: Cursor? = null
        when (match) {
            ALL -> {
                val all = delegate.all
                values = ArrayList(all.size)
                types = ArrayList(all.size)
                keys = ArrayList(all.size)
                for ((key1, value1) in all) {
                    key = key1
                    value = value1
                    type = Preferences.ALL
                    when (value) {
                        is Boolean -> {
                            type = Preferences.BOOLEAN
                            value = if (value) 1 else 0
                        }
                        is Float -> {
                            type = Preferences.FLOAT
                        }
                        is Int -> {
                            type = Preferences.INT
                        }
                        is Long -> {
                            type = Preferences.LONG
                        }
                        is CharSequence -> {
                            type = Preferences.STRING
                        }
                        is Set<*> -> {
                            type = Preferences.STRING_SET
                            value = fromStringSet(value as Set<String>?)
                        }
                    }

                    types.add(type)
                    keys.add(key)
                    values.add(value)
                }
                cursor = PreferencesCursor(types, keys, values)
            }
            ALL_KEY -> {
                type = Preferences.ALL
                if (delegate.contains(key)) {
                    value = delegate.all[key]
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            BOOLEAN -> {
                type = Preferences.BOOLEAN
                if (delegate.contains(key)) {
                    value = if (delegate.getBoolean(key, false)) 1 else 0
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            FLOAT -> {
                type = Preferences.FLOAT
                if (delegate.contains(key)) {
                    value = delegate.getFloat(key, 0f)
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            INT -> {
                type = Preferences.INT
                if (delegate.contains(key)) {
                    value = delegate.getInt(key, 0)
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            LONG -> {
                type = Preferences.LONG
                if (delegate.contains(key)) {
                    value = delegate.getLong(key, 0)
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            STRING -> {
                type = Preferences.STRING
                if (delegate.contains(key)) {
                    value = delegate.getString(key, null)
                    cursor = PreferencesCursor(type, key, value)
                } else {
                    cursor = PreferencesCursor()
                }
            }
            STRING_SET -> {
                type = Preferences.STRING_SET
                if (delegate.contains(key)) {
                    val set = delegate.getStringSet(key, null)
                    values = ArrayList()
                    if (set != null) {
                        values.addAll(set)
                    }
                    types = ArrayList(values.size)
                    types.fill(type)
                    keys = ArrayList(values.size)
                    keys.fill(key)
                    cursor = PreferencesCursor(types, keys, values)
                } else {
                    cursor = PreferencesCursor()
                }
            }
        }
        context?.let { cursor?.setNotificationUri(it.contentResolver, uri) }
        return cursor
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return if (update(uri, values, null, null) > 0) uri else null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val delegate = this.delegate ?: return 0
        val match = uriMatcher.match(uri)
        var result = 0
        when (match) {
            ALL -> {
                delegate.edit().clear().apply()
                result = 1
            }
            else -> {
                val key = uri.lastPathSegment
                if (delegate.contains(key)) {
                    delegate.edit().remove(key).apply()
                    result = 1
                }
            }
        }
        return result
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        if (values == null) return 0
        val delegate = this.delegate ?: return 0
        var key = uri.lastPathSegment!!
        val match = uriMatcher.match(uri)
        val editor = delegate.edit()
        var result = 0
        when (match) {
            ALL -> {
                var value: Any?
                for (k in values.keySet()) {
                    key = k
                    value = values[key]
                    when (value) {
                        is Boolean -> {
                            editor.putBoolean(key, values.getAsBoolean(key))
                            result++
                        }
                        is Float -> {
                            editor.putFloat(key, values.getAsFloat(key))
                            result++
                        }
                        is Int -> {
                            editor.putInt(key, values.getAsInteger(key))
                            result++
                        }
                        is Long -> {
                            editor.putLong(key, values.getAsLong(key))
                            result++
                        }
                        is CharSequence -> {
                            editor.putString(key, values.getAsString(key))
                            result++
                        }
                    }
                }
            }
            BOOLEAN -> {
                editor.putBoolean(key, values.getAsBoolean(Preferences.Columns.VALUE))
                result++
            }
            FLOAT -> {
                editor.putFloat(key, values.getAsFloat(Preferences.Columns.VALUE))
                result++
            }
            INT -> {
                editor.putInt(key, values.getAsInteger(Preferences.Columns.VALUE))
                result++
            }
            LONG -> {
                editor.putLong(key, values.getAsLong(Preferences.Columns.VALUE))
                result++
            }
            STRING -> {
                editor.putString(key, values.getAsString(Preferences.Columns.VALUE))
                result++
            }
            STRING_SET -> {
                editor.putStringSet(key, toStringSet(values.getAsString(Preferences.Columns.VALUE)))
                result++
            }
        }
        editor.apply()
        return result
    }

    companion object {
        private const val ALL = 1
        private const val BOOLEAN = 2
        private const val FLOAT = 3
        private const val INT = 4
        private const val LONG = 5
        private const val STRING = 6
        private const val STRING_SET = 7
        private const val ALL_KEY = 8
    }
}