/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.media

import android.database.Cursor
import android.database.CursorWrapper
import android.net.Uri

/**
 * Cursor that adds the user id to fetched URIs. This is especially useful for [RingtoneManager.getCursor] as
 * a managed profile should also list its parent's ringtones
 */
class ExternalRingtonesCursorWrapper(cursor: Cursor, private val uri: Uri) :
    CursorWrapper(cursor) {
    override fun getString(index: Int): String {
        return if (index == RingtoneManager.URI_COLUMN_INDEX)
            uri.toString()
        else
            super.getString(index)
    }
}