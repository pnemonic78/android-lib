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
import android.view.View
import android.widget.NumberPicker

open class NumberPreferenceDialog : PreferenceDialog() {

    private var picker: NumberPicker? = null

    override fun onBindDialogView(view: View) {
        super.onBindDialogView(view)
        val picker: NumberPicker? = view.findViewById(android.R.id.edit)
        checkNotNull(picker) { "Dialog view must contain a NumberPicker with id 'edit'" }
        this.picker = picker
        val preference = numberPickerPreference
        picker.minValue = preference.min
        picker.maxValue = preference.max
        picker.value = preference.value
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        if (positiveResult) {
            picker!!.clearFocus()
            val value = picker!!.value
            val preference = numberPickerPreference
            if (preference.callChangeListener(value)) {
                preference.value = value
            }
        }
    }

    private val numberPickerPreference: NumberPickerPreference
        get() = preference as NumberPickerPreference

    companion object {
        fun newInstance(key: String): NumberPreferenceDialog {
            val dialog = NumberPreferenceDialog()
            val b = Bundle(1)
            b.putString(ARG_KEY, key)
            dialog.arguments = b
            return dialog
        }
    }
}