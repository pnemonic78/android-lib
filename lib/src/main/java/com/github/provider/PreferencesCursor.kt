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

import android.database.MatrixCursor

/**
 * Cursor wrapper for a single preference value.
 *
 * @author Moshe Waisberg
 */
class PreferencesCursor : MatrixCursor {
    constructor() : super(
        arrayOf<String>(
            Preferences.Columns.KEY,
            Preferences.Columns.VALUE,
            Preferences.Columns.TYPE
        ),
        0
    )

    constructor(
        type: String,
        key: String,
        value: Any?
    ) : super(
        arrayOf<String>(
            Preferences.Columns.KEY,
            Preferences.Columns.VALUE,
            Preferences.Columns.TYPE
        ),
        1
    ) {
        addRow(arrayOf(key, value, type))
    }

    constructor(types: List<String?>, keys: List<String?>, values: List<Any?>) : super(
        arrayOf<String>(
            Preferences.Columns.KEY,
            Preferences.Columns.VALUE,
            Preferences.Columns.TYPE
        ),
        values.size
    ) {
        val count = values.size
        val row = arrayOfNulls<Any>(3)
        for (i in 0 until count) {
            row[INDEX_TYPE] = types[i]
            row[INDEX_KEY] = keys[i]
            row[INDEX_VALUE] = values[i]
            addRow(row)
        }
    }

    override fun getColumnIndex(columnName: String): Int {
        return when (columnName) {
            Preferences.Columns.KEY -> INDEX_KEY
            Preferences.Columns.VALUE -> INDEX_VALUE
            Preferences.Columns.TYPE -> INDEX_TYPE
            else -> -1
        }
    }

    companion object {
        private const val INDEX_KEY = 0
        private const val INDEX_VALUE = 1
        private const val INDEX_TYPE = 2
    }
}