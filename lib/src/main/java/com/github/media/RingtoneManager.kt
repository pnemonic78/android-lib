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
package com.github.media

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.database.Cursor
import android.media.Ringtone
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import androidx.core.content.PermissionChecker
import com.github.lib.R
import java.io.File

/**
 * Ringtone manager that can ignore external media when not permitted.
 */
class RingtoneManager(private val context: Context) : android.media.RingtoneManager(context) {

    private var isInit = false
    private var cursor: Cursor? = null
    private var type = 0

    /**
     * Is external media included?
     */
    var isIncludeExternal = true

    /**
     * If a column (item from this list) exists in the Cursor, its value must
     * be true (value of 1) for the row to be returned.
     */
    private val filterColumns = mutableListOf<String>()

    init {
        isInit = true
        isIncludeExternal = PermissionChecker.checkCallingOrSelfPermission(
            context,
            PERMISSION_RINGTONE
        ) == PermissionChecker.PERMISSION_GRANTED
        if (type > 0) {
            setFilterColumnsList(type)
        }
    }

    override fun setType(type: Int) {
        cursor?.close()
        cursor = null
        super.setType(type)
        this.type = type
        if (isInit) {
            setFilterColumnsList(type)
        }
    }

    override fun getCursor(): Cursor {
        var cursor = cursor
        if (cursor != null && cursor.isClosed) {
            cursor = null
            this.cursor = null
        }
        if (cursor == null) {
            if (isIncludeExternal) {
                try {
                    cursor = super.getCursor()
                } catch (e: SecurityException) {
                    isIncludeExternal = false
                    cursor = getInternalRingtones()
                }
            } else {
                cursor = getInternalRingtones()
            }
            this.cursor = cursor
        }
        return cursor!!
    }

    private fun getInternalRingtones(): Cursor {
        val res = query(
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI, INTERNAL_COLUMNS,
            constructBooleanTrueWhereClause(filterColumns),
            null,
            MediaStore.Audio.Media.DEFAULT_SORT_ORDER
        )
        return ExternalRingtonesCursorWrapper(
            res!!,
            MediaStore.Audio.Media.INTERNAL_CONTENT_URI
        )
    }

    private fun setFilterColumnsList(type: Int) {
        val columns = filterColumns
        columns.clear()

        if (type and TYPE_RINGTONE != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_RINGTONE)
        }
        if (type and TYPE_NOTIFICATION != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_NOTIFICATION)
        }
        if (type and TYPE_ALARM != 0) {
            columns.add(MediaStore.Audio.AudioColumns.IS_ALARM)
        }
    }

    private fun query(
        uri: Uri,
        projection: Array<String>? = null,
        selection: String? = null,
        selectionArgs: Array<String>? = null,
        sortOrder: String? = null
    ): Cursor? {
        return context.contentResolver.query(uri, projection, selection, selectionArgs, sortOrder)
    }

    fun filterInternal(uriStringDangerous: String?): String? {
        var uriString = uriStringDangerous ?: return null
        if (uriString.isEmpty()) return uriString
        if (uriString.startsWith(INTERNAL_PATH)) {
            // Is definitely internal.
            return uriString
        }
        var dangerousUri = false
        if (uriString.startsWith(EXTERNAL_PATH)) {
            dangerousUri = true
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            dangerousUri = if (uriString.startsWith(FILE_PATH)) {
                true
            } else {
                val file = File(uriString)
                file.exists() && file.isFile
            }
        }
        if (dangerousUri) {
            // Try a 'default' tone.
            uriString = getDefaultUri(type).toString()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                if (uriString.startsWith(FILE_PATH)) {
                    return SILENT_PATH
                }
                val file = File(uriString)
                if (file.exists() && file.isFile) {
                    return SILENT_PATH
                }
            }
        }
        val uri = Uri.parse(uriString)
        val uriResolved = resolveUri(context, uri)
        if (uri !== uriResolved) {
            if (uriResolved == null) {
                return DEFAULT_PATH
            }
            val uriResolvedString = uriResolved.toString()
            if (uriResolvedString.startsWith(INTERNAL_PATH)) {
                // Is definitely internal.
                return uriString
            }
            if (uriResolvedString.startsWith(EXTERNAL_PATH)) {
                // 'Default' tone is definitely external.
                return SILENT_PATH
            }
        }
        return uriString
    }

    fun filterInternal(uri: Uri?): String? {
        return filterInternal(uri?.toString())
    }

    fun filterInternalMaybe(uri: Uri?): String? {
        return filterInternalMaybe(uri?.toString())
    }

    fun filterInternalMaybe(uriString: String?): String? {
        return if (isIncludeExternal) uriString else filterInternal(uriString)
    }

    /**
     * Get the 'default' tone title.
     *
     * @return the title.
     */
    val defaultTitle: String
        get() {
            return when (type) {
                TYPE_NOTIFICATION -> context.getString(R.string.notification_sound_default)
                TYPE_ALARM -> context.getString(R.string.alarm_sound_default)
                else -> context.getString(R.string.ringtone_default)
            }
        }

    /**
     * Get the 'silent' tone title.
     *
     * @return the title.
     */
    val silentTitle: String
        get() = context.getString(R.string.ringtone_silent)

    companion object {
        const val TYPE_RINGTONE = android.media.RingtoneManager.TYPE_RINGTONE
        const val TYPE_NOTIFICATION = android.media.RingtoneManager.TYPE_NOTIFICATION
        const val TYPE_ALARM = android.media.RingtoneManager.TYPE_ALARM
        const val TYPE_ALL = android.media.RingtoneManager.TYPE_ALL
        const val EXTRA_RINGTONE_SHOW_DEFAULT =
            android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT
        const val EXTRA_RINGTONE_SHOW_SILENT =
            android.media.RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT

        /**
         * Invalid [Uri] path that means 'default'.
         */
        @JvmField
        val DEFAULT_PATH: String? = null

        /**
         * Empty [Uri] that means 'silent'.
         */
        @JvmField
        val SILENT_URI = Uri.EMPTY

        /**
         * Empty [Uri] path that means 'silent'.
         */
        @JvmField
        val SILENT_PATH = SILENT_URI.toString()

        private val INTERNAL_PATH = MediaStore.Audio.Media.INTERNAL_CONTENT_URI.toString()
        private val EXTERNAL_PATH = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI.toString()
        private val SETTINGS_PATH = Settings.System.CONTENT_URI.toString()
        private const val FILE_PATH = ContentResolver.SCHEME_FILE + ":/"

        private val INTERNAL_COLUMNS = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.TITLE_KEY
        )
        private val SETTINGS_COLUMNS = arrayOf(
            Settings.NameValueTable._ID,
            Settings.NameValueTable.NAME,
            Settings.NameValueTable.VALUE
        )

        /**
         * The column index (in the cursor} for the row ID.
         */
        const val ID_COLUMN_INDEX = 0

        /**
         * The column index (in the cursor} for the title.
         */
        const val TITLE_COLUMN_INDEX = 1

        /**
         * The column index (in the cursor} for the media provider's URI.
         */
        const val URI_COLUMN_INDEX = 2

        @JvmStatic
        val PERMISSION_RINGTONE = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_AUDIO
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

        /**
         * Constructs a where clause that consists of at least one column being 1
         * (true). This is used to find all matching sounds for the given sound
         * types (ringtone, notifications, etc.)
         *
         * @param columns The columns that must be true.
         * @return The where clause.
         */
        private fun constructBooleanTrueWhereClause(columns: List<String>?): String? {
            if (columns.isNullOrEmpty()) return null
            val size = columns.size
            val sb = StringBuilder()
            sb.append('(')
            for (i in 0 until size) {
                sb.append(columns[i]).append("=1 or ")
            }
            // Remove last ' or '
            sb.setLength(sb.length - 4)
            sb.append(')')
            return sb.toString()
        }

        @JvmStatic
        fun resolveUri(context: Context, uri: Uri): Uri? {
            val uriString = uri.toString()
            if (uriString.startsWith(SETTINGS_PATH)) {
                val resolver = context.contentResolver
                val cursor = resolver.query(uri, SETTINGS_COLUMNS, null, null, null)
                if (cursor != null && cursor.moveToFirst()) {
                    val path = cursor.getString(URI_COLUMN_INDEX)
                    cursor.close()
                    return if (path == null) null else Uri.parse(path)
                }
            }
            return uri
        }

        fun getRingtone(context: Context, uri: Uri?): Ringtone? =
            android.media.RingtoneManager.getRingtone(context, uri)

        fun getDefaultUri(type: Int): Uri? = android.media.RingtoneManager.getDefaultUri(type)
    }
}