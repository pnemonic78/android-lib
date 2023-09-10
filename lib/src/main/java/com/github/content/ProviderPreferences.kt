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
package com.github.content

import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.Context
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.net.Uri
import com.github.provider.Preferences
import timber.log.Timber

/**
 * Shared preferences that are sharable across processes by using a content provider.
 *
 * @author Moshe Waisberg
 */
class ProviderPreferences(private val context: Context) : SharedPreferences,
    SharedPreferences.Editor {

    private val resolver: ContentResolver
    private val contentUri: Uri
    private val ops = ArrayList<ContentProviderOperation>()

    init {
        resolver = context.contentResolver
        contentUri = Preferences.CONTENT_URI(context)
    }

    override fun getAll(): Map<String, *> {
        val result: MutableMap<String, Any> = mutableMapOf()
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.ALL)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                var key: String
                do {
                    key = cursor.getString(INDEX_KEY)
                    when (cursor.getString(INDEX_TYPE)) {
                        Preferences.BOOLEAN -> result[key] = cursor.getInt(INDEX_VALUE) != 0
                        Preferences.FLOAT -> result[key] = cursor.getFloat(INDEX_VALUE)
                        Preferences.INT -> result[key] = cursor.getInt(INDEX_VALUE)
                        Preferences.LONG -> result[key] = cursor.getLong(INDEX_VALUE)
                        Preferences.STRING -> result[key] = cursor.getString(INDEX_VALUE)
                        Preferences.STRING_SET ->
                            result[key] = Preferences.toStringSet(cursor.getString(INDEX_VALUE))
                                ?: emptySet<String>()
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return result
    }

    override fun getBoolean(key: String, defValue: Boolean): Boolean {
        var result = defValue
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.BOOLEAN)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(INDEX_VALUE) != 0
            }
            cursor.close()
        }
        return result
    }

    override fun getFloat(key: String, defValue: Float): Float {
        var result = defValue
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.FLOAT)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getFloat(INDEX_VALUE)
            }
            cursor.close()
        }
        return result
    }

    override fun getInt(key: String, defValue: Int): Int {
        var result = defValue
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.INT)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getInt(INDEX_VALUE)
            }
            cursor.close()
        }
        return result
    }

    override fun getLong(key: String, defValue: Long): Long {
        var result = defValue
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.LONG)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getLong(INDEX_VALUE)
            }
            cursor.close()
        }
        return result
    }

    override fun getString(key: String, defValue: String?): String? {
        var result = defValue
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.STRING)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = cursor.getString(INDEX_VALUE)
            }
            cursor.close()
        }
        return result
    }

    override fun getStringSet(key: String, defValues: Set<String>?): Set<String>? {
        var result = defValues
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.STRING_SET)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                result = mutableSetOf()
                do {
                    result.add(cursor.getString(INDEX_VALUE))
                } while (cursor.moveToNext())
            }
            cursor.close()
        }
        return result
    }

    override fun contains(key: String): Boolean {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.BOOLEAN)
            .appendEncodedPath(key)
            .build()
        val cursor = resolver.query(uri, null, null, null, null)
        var result = false
        if (cursor != null) {
            result = cursor.count > 0
            cursor.close()
        }
        return result
    }

    override fun edit(): SharedPreferences.Editor {
        return this
    }

    override fun registerOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        // TODO Implemented me!
    }

    override fun unregisterOnSharedPreferenceChangeListener(listener: OnSharedPreferenceChangeListener) {
        // TODO Implemented me!
    }

    override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.BOOLEAN)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, value)
            .build()
        ops.add(op)
        return this
    }

    override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.FLOAT)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, value)
            .build()
        ops.add(op)
        return this
    }

    override fun putInt(key: String, value: Int): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.INT)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, value)
            .build()
        ops.add(op)
        return this
    }

    override fun putLong(key: String, value: Long): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.LONG)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, value)
            .build()
        ops.add(op)
        return this
    }

    override fun putString(key: String, value: String?): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.STRING)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, value)
            .build()
        ops.add(op)
        return this
    }

    override fun putStringSet(key: String, values: Set<String>?): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.STRING_SET)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newUpdate(uri)
            .withValue(Preferences.Columns.VALUE, Preferences.fromStringSet(values))
            .build()
        ops.add(op)
        return this
    }

    override fun remove(key: String): SharedPreferences.Editor {
        val uri = contentUri.buildUpon()
            .appendPath(Preferences.ALL)
            .appendEncodedPath(key)
            .build()
        val op = ContentProviderOperation.newDelete(uri)
            .build()
        ops.add(op)
        return this
    }

    override fun clear(): SharedPreferences.Editor {
        val uri = contentUri
        val op = ContentProviderOperation.newDelete(uri).build()
        ops.add(op)
        return this
    }

    override fun commit(): Boolean {
        try {
            resolver.applyBatch(Preferences.AUTHORITY(context), ops)
            ops.clear()
            return true
        } catch (e: Exception) {
            Timber.e(e)
        }
        return false
    }

    override fun apply() {
        commit()
    }

    companion object {
        private const val INDEX_KEY = 0
        private const val INDEX_VALUE = 1
        private const val INDEX_TYPE = 2
    }
}