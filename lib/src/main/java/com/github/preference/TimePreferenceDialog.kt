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

import android.os.Bundle
import android.text.format.DateFormat
import android.view.View
import android.widget.TimePicker
import com.github.util.hour
import com.github.util.minute
import com.github.widget.hours
import com.github.widget.minutes
import java.util.Calendar

open class TimePreferenceDialog : PreferenceDialog() {

    private var picker: TimePicker? = null

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        val context = view.context
        val picker = view.findViewById<TimePicker>(android.R.id.edit)
        checkNotNull(picker) { "Dialog view must contain a TimePicker with id 'edit'" }
        this.picker = picker
        picker.setIs24HourView(DateFormat.is24HourFormat(context))
        timePreference.time?.let { time ->
            picker.hours = time.hour
            picker.minutes = time.minute
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val picker = this.picker ?: return
        if (positiveResult) {
            val time = Calendar.getInstance()
            time.hour = picker.hours
            time.minute = picker.minutes
            val preference = timePreference
            if (preference.callChangeListener(time)) {
                preference.setTime(time)
            }
        }
    }

    private val timePreference: TimePreference
        get() = preference as TimePreference

    companion object {
        fun newInstance(key: String): TimePreferenceDialog {
            return TimePreferenceDialog().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
        }
    }
}