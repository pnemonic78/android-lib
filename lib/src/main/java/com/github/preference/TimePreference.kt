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

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.github.lib.R
import com.github.util.TypedValueUtils
import timber.log.Timber
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Preference that shows a time picker.
 *
 *
 * The preference value is stored in the ISO 8601 format `hh:mm`
 *
 *
 * @author Moshe Waisberg
 * @see [ISO 8601 - Wikipedia](https://en.wikipedia.org/wiki/ISO_8601.Times)
 */
open class TimePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = TypedValueUtils.getAttr(
        context,
        androidx.preference.R.attr.dialogPreferenceStyle,
        android.R.attr.dialogPreferenceStyle
    ),
    @StyleRes defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {
    /**
     * Gets the time from the [SharedPreferences].
     *
     * @return The current preference value.
     */
    var value: String? = null
        private set

    /**
     * Gets the time from the [SharedPreferences].
     *
     * @return The current preference value.
     */
    var time: Calendar? = null
        private set

    init {
        if (dialogLayoutResource == 0) {
            dialogLayoutResource = R.layout.preference_dialog_timepicker
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): String? {
        return a.getString(index)
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        setTime(getPersistedString(defaultValue as String?))
    }

    override fun shouldDisableDependents(): Boolean {
        return value.isNullOrEmpty() || super.shouldDisableDependents()
    }

    /**
     * Saves the time to the [SharedPreferences].
     *
     * @param timeString The chosen time. Can be `null`.
     */
    fun setTime(timeString: String?) {
        val wasBlocking = shouldDisableDependents()
        value = timeString
        val time = parseTime(timeString)
        this.time = time

        if (time != null) {
            persistString(timeString)
            notifyChanged()
        } else if (!timeString.isNullOrEmpty()) {
            Timber.e("invalid time [%s] for preference [%s]", timeString, key)
        }

        val isBlocking = shouldDisableDependents()
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking)
        }
    }

    /**
     * Saves the time to the [SharedPreferences].
     *
     * @param time The chosen time. Can be `null`.
     */
    fun setTime(time: Calendar?) {
        val wasBlocking = shouldDisableDependents()
        val timeString = if (time != null) formatIso.format(time.time) else null
        value = timeString
        this.time = time

        persistString(timeString)
        notifyChanged()

        val isBlocking = shouldDisableDependents()
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking)
        }
    }

    override fun onNeutralButtonClicked() {
        if (callChangeListener(null)) {
            setTime(null as Calendar?)
        }
    }

    companion object {
        /**
         * ISO 8601 time format.
         */
        private const val PATTERN = "HH:mm"

        private val formatIso: DateFormat = SimpleDateFormat(PATTERN, Locale.US)

        /**
         * Parse the time value.
         *
         * @param timeString the time in ISO 8601 format.
         * @return the time - `null` otherwise.
         */
        @JvmStatic
        fun parseTime(timeString: String?): Calendar? {
            if (timeString.isNullOrEmpty()) return null
            try {
                val date = formatIso.parse(timeString)!!
                val time = Calendar.getInstance()
                time.time = date
                return time
            } catch (e: ParseException) {
                Timber.e(e, "parse time: %s", timeString)
            }
            return null
        }
    }
}