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
package com.github.preference

import android.app.Activity
import android.content.ContentUris
import android.content.Context
import android.content.res.TypedArray
import android.media.AudioManager
import android.media.Ringtone
import android.net.Uri
import android.util.AttributeSet
import androidx.preference.R
import com.github.media.RingtoneManager
import com.github.util.TypedValueUtils.getAttr
import timber.log.Timber

/**
 * A [Preference] that allows the user to choose a ringtone from those on the device.
 * The chosen ringtone's URI will be persisted as a string.
 *
 * @author Moshe Waisberg
 */
open class RingtonePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.ringtonePreferenceStyle),
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr) {

    /**
     * The ringotone type.
     * One of [RingtoneManager.TYPE_ALARM] or [RingtoneManager.TYPE_NOTIFICATION] or [RingtoneManager.TYPE_RINGTONE], or [RingtoneManager.TYPE_ALL].
     */
    var ringtoneType = RingtoneManager.TYPE_RINGTONE
        set(value) {
            field = value
            setRingtoneType(value, false)
        }

    /**
     * Whether to show an item for the default sound/ringtone. The default
     * to use will be deduced from the sound type(s) being shown.
     *
     * @see [RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT]
     */
    var showDefault = false

    /**
     * Whether to show an item for 'Silent'.
     *
     * @see [RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT]
     */
    var showSilent = false

    private var entries: List<CharSequence?>? = null
    var entryValues: List<Uri?>? = null
        private set
    private var ringtoneManager: RingtoneManager? = null
    private var ringtoneSample: Ringtone? = null

    /**
     * The position in the list of the 'Silent' item.
     */
    private var silentPos = POS_UNKNOWN

    /**
     * The position in the list of the 'Default' item.
     */
    private var defaultRingtonePos = POS_UNKNOWN

    /**
     * The Uri to play when the 'Default' item is clicked.
     */
    private var defaultRingtoneUri: Uri? = null
    private var defaultValue = DEFAULT_PATH

    /**
     * A Ringtone for the default ringtone. In most cases, the RingtoneManager
     * will stop the previous ringtone. However, the RingtoneManager doesn't
     * manage the default ringtone for us, so we should stop this one manually.
     */
    private var defaultRingtone: Ringtone? = null

    init {
        var ringtoneType: Int = this.ringtoneType
        var showDefault = false
        var showSilent = false

        val a = context.obtainStyledAttributes(attrs, ATTRIBUTES, defStyleAttr, defStyleRes)
        val count = a.indexCount
        for (i in 0 until count) {
            val index = a.getIndex(i)
            when (ATTRIBUTES[i]) {
                android.R.attr.ringtoneType -> ringtoneType = a.getInt(index, ringtoneType)
                android.R.attr.showDefault -> showDefault = a.getBoolean(index, showDefault)
                android.R.attr.showSilent -> showSilent = a.getBoolean(index, showSilent)
                android.R.attr.defaultValue -> defaultValue = onGetDefaultValue(a, index)
            }
        }
        a.recycle()

        ringtoneManager = RingtoneManager(context)
        this.ringtoneType = ringtoneType
        this.showDefault = showDefault
        this.showSilent = showSilent
    }

    protected fun setRingtoneType(type: Int, force: Boolean) {
        val context = context
        if (type != ringtoneType || force) {
            if (entries != null) {
                ringtoneManager = RingtoneManager(context)
                entries = null
                entryValues = null
            }
            ringtoneManager?.setType(type)

            // Switch to the other default tone?
            var value = value
            var preserveDefault = false
            if (!value.isNullOrEmpty()) {
                val valueUri = Uri.parse(value)
                val defaultUri = defaultRingtoneUri ?: android.media.RingtoneManager.getDefaultUri(ringtoneType)
                preserveDefault = (valueUri == defaultUri)
            }

            defaultRingtoneUri = android.media.RingtoneManager.getDefaultUri(type)
            defaultRingtone = android.media.RingtoneManager.getRingtone(context, defaultRingtoneUri)

            if (preserveDefault && defaultRingtoneUri != null) {
                value = ringtoneManager?.filterInternalMaybe(defaultRingtoneUri)
                setDefaultValue(value)
                getEntries() // Rebuild the entries for change listener.
                if (callChangeListener(value)) {
                    onSaveRingtone(defaultRingtoneUri)
                    notifyChanged()
                }
            }
        }

        // The volume keys will control the stream that we are choosing a ringtone for
        if (context is Activity) {
            context.volumeControlStream = ringtoneManager?.inferStreamType() ?: AudioManager.STREAM_RING
        }
    }

    /**
     * Called when a ringtone is chosen.
     *
     *
     * By default, this saves the ringtone URI to the persistent storage as a
     * string.
     *
     * @param ringtoneUri The chosen ringtone's [Uri]. Can be `null`.
     */
    protected open fun onSaveRingtone(ringtoneUri: Uri?) {
        persistString(ringtoneUri?.toString() ?: SILENT_PATH)
    }

    /**
     * Called when the chooser is about to be shown and the current ringtone
     * should be marked. Can return null to not mark any ringtone.
     *
     * By default, this restores the previous ringtone URI from the persistent
     * storage.
     *
     * @return The ringtone to be marked as the current ringtone.
     */
    protected open fun onRestoreRingtone(): Uri? {
        val uriString = value
        if (uriString === DEFAULT_PATH) {
            return defaultRingtoneUri
        }
        return if (uriString.isNullOrEmpty()) SILENT_URI else Uri.parse(uriString)
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): String? {
        return a.getString(index)
    }

    override fun onSetInitialValue(defaultValueObj: Any?) {
        val defaultValue = getPersistedString(defaultValueObj as String?)

        // If we are setting to the default value, we should persist it.
        if (!defaultValue.isNullOrEmpty()) {
            onSaveRingtone(Uri.parse(defaultValue))
        }
    }

    /**
     * Returns the index of the given value (in the entry values array).
     *
     * @param value The value whose index should be returned.
     * @return The index of the value, or -1 if not found.
     */
    fun findIndexOfValue(value: Uri?): Int {
        val entryValues = entryValues ?: return POS_UNKNOWN
        if ((value === DEFAULT_URI) || (value == defaultRingtoneUri)) {
            return defaultRingtonePos
        }
        if (SILENT_URI == value) {
            return silentPos
        }

        var entryValue: Uri?
        for (i in entryValues.indices.reversed()) {
            entryValue = entryValues[i]
            if (value == entryValue) {
                return i
            }
        }
        return POS_UNKNOWN
    }

    val valueIndex: Int
        get() = findIndexOfValue(onRestoreRingtone())

    fun getEntries(): List<CharSequence?> {
        var entries = entries
        var entryValues = entryValues
        if (entries == null || entryValues == null) {
            entries = mutableListOf()
            entryValues = mutableListOf()
            this.entries = entries
            this.entryValues = entryValues
            val manager = ringtoneManager!!

            if (showDefault) {
                val uriPath = manager.filterInternalMaybe(defaultRingtoneUri)
                if (uriPath != null) {
                    defaultRingtonePos = entryValues.size
                    entries.add(manager.defaultTitle)
                    entryValues.add(defaultRingtoneUri)
                } else {
                    defaultRingtonePos = POS_UNKNOWN
                }
            } else {
                defaultRingtonePos = POS_UNKNOWN
            }
            if (showSilent) {
                silentPos = entryValues.size
                entries.add(manager.silentTitle)
                entryValues.add(SILENT_URI)
            } else {
                silentPos = -1
            }

            val cursor = manager.cursor
            if (cursor.moveToFirst()) {
                var uri: Uri?
                do {
                    uri = Uri.parse(cursor.getString(RingtoneManager.URI_COLUMN_INDEX))
                    entries.add(cursor.getString(RingtoneManager.TITLE_COLUMN_INDEX))
                    entryValues.add(ContentUris.withAppendedId(uri, cursor.getLong(RingtoneManager.ID_COLUMN_INDEX)))
                } while (cursor.moveToNext())
                cursor.close()
            }
        }
        return entries
    }

    fun playRingtone(position: Int) {
        ringtoneSample?.stop()

        val ringtone: Ringtone? = if (position == silentPos) {
            null
        } else if (position == defaultRingtonePos) {
            defaultRingtone
        } else {
            RingtoneManager.getRingtone(context, getRingtoneUri(position))
        }
        ringtone?.play()
        ringtoneSample = ringtone
    }

    fun stopAnyPlayingRingtone() {
        ringtoneSample?.stop()
        ringtoneManager?.stopPreviousRingtone()
    }

    fun getRingtoneUri(position: Int): Uri? {
        return entryValues?.get(position)
    }

    /**
     * Sets the value of the key. This should be one of the entries.
     *
     * @param value The value to set for the key
     */
    fun setValue(value: Uri?) {
        this.value = value?.toString()
    }

    /**
     * The value of the key. This should be one of the entries.
     */
    var value: String?
        get() = ringtoneManager?.filterInternalMaybe(getPersistedString(defaultValue))
        set(value) {
            // Always persist/notify the first time.
            val oldValue = getPersistedString(defaultValue)
            if (oldValue != value) {
                persistString(value)
                notifyChanged()
            }
        }

    /**
     * Get the ringtone title.
     */
    val ringtoneTitle: CharSequence?
        get() = getRingtoneTitle(value)

    /**
     * Get the ringtone title.
     *
     * @param uriString the Uri path.
     * @return the title.
     */
    fun getRingtoneTitle(uriString: String?): CharSequence? {
        if (uriString === DEFAULT_PATH) {
            return ringtoneManager?.defaultTitle
        }
        if (uriString == SILENT_PATH) {
            return ringtoneManager?.silentTitle
        }
        val uri = Uri.parse(uriString)
        val index = findIndexOfValue(uri)
        if (index > POS_UNKNOWN) {
            return entries!![index]
        }
        val context = context
        val ringtone: Ringtone? = RingtoneManager.getRingtone(context, uri)
        if (ringtone != null) {
            try {
                return ringtone.getTitle(context)
            } catch (e: Exception) {
                Timber.e(e)
            }
        }
        return null
    }

    companion object {
        private val ATTRIBUTES = intArrayOf(android.R.attr.ringtoneType, android.R.attr.showDefault, android.R.attr.showSilent, android.R.attr.defaultValue).sortedArray()
        private val DEFAULT_PATH = RingtoneManager.DEFAULT_PATH
        private val DEFAULT_URI: Uri? = null
        val SILENT_PATH = RingtoneManager.SILENT_PATH
        private val SILENT_URI = RingtoneManager.SILENT_URI
        private const val POS_UNKNOWN = -1
    }
}