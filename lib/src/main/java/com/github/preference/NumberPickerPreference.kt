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
import com.github.lib.R

/**
 * Preference that shows a number picker.
 *
 * @author Moshe Waisberg
 */
open class NumberPickerPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * The value.
     */
    var value = 0
        set(value) {
            field = setValueImpl(field, value)
        }

    /**
     * The range of the value bar to `0`...`max`.
     */
    var max: Int

    /**
     * The minimum value.
     */
    var min = 0

    private var progressSet = false

    init {
        val a = context.obtainStyledAttributes(attrs, ATTRIBUTES, defStyleAttr, defStyleRes)
        max = a.getInt(0, 100)
        a.recycle()
        if (dialogLayoutResource == 0) {
            dialogLayoutResource = R.layout.preference_dialog_numberpicker
        }
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Int? {
        return if (a.hasValue(0)) a.getInt(index, 0) else null
    }

    override fun onSetInitialValue(defaultValue: Any?) {
        value = getPersistedInt((defaultValue as? Int) ?: value)
    }

    /**
     * Saves the value to the [SharedPreferences].
     *
     * @param value the value.
     */
    private fun setValueImpl(oldValue: Int, newValue: Int): Int {
        // Always persist/notify the first time; don't assume the field's default value.
        val changed = oldValue != newValue
        if (changed || !progressSet) {
            progressSet = true
            persistInt(newValue)
            if (changed) {
                notifyDependencyChange(shouldDisableDependents())
                notifyChanged()
            }
        }
        return newValue
    }

    companion object {
        private val ATTRIBUTES = intArrayOf(android.R.attr.max)
    }
}