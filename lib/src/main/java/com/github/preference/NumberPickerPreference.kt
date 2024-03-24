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
import android.os.Build
import android.util.AttributeSet
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import com.github.lib.R
import com.github.util.TypedValueUtils
import kotlin.math.max
import kotlin.math.min

/**
 * Preference that shows a number picker.
 *
 * @author Moshe Waisberg
 */
open class NumberPickerPreference @JvmOverloads constructor(
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
     * The value.
     */
    var value = 0
        set(value) {
            field = setValueImpl(field, value)
        }

    /**
     * The upper bound.
     */
    var max: Int = 100
        set(value) {
            val newValue = max(min, value)
            if (newValue != field) {
                field = newValue
                notifyChanged()
            }
        }

    /**
     * The lower bound.
     */
    var min = 0
        set(value) {
            val newValue = min(max, value)
            if (newValue != field) {
                field = newValue
                notifyChanged()
            }
        }

    var displayedValues: Array<String?>? = null
        protected set

    private var progressSet = false

    init {
        var a = context.obtainStyledAttributes(
            attrs,
            androidx.preference.R.styleable.ListPreference,
            defStyleAttr,
            defStyleRes
        )
        displayedValues = TypedValueUtils.getStringArray(
            a,
            androidx.preference.R.styleable.ListPreference_entryValues,
            androidx.preference.R.styleable.ListPreference_android_entryValues
        )
        a.recycle()

        a = context.obtainStyledAttributes(
            attrs,
            R.styleable.NumberPickerPreference,
            defStyleAttr,
            defStyleRes
        )
        min = max(0, a.getInt(R.styleable.NumberPickerPreference_min, min))
        max = max(0, a.getInt(R.styleable.NumberPickerPreference_android_max, max))
        if (a.getBoolean(R.styleable.NumberPickerPreference_useSimpleSummaryProvider, false)) {
            summaryProvider = SimpleSummaryProvider.instance
        }
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
     * @param oldValue The old value.
     * @param newValue The new value.
     */
    protected open fun setValueImpl(oldValue: Int, newValue: Int): Int {
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

    /**
     * A simple [androidx.preference.Preference.SummaryProvider] implementation for a
     * [NumberPickerPreference]. If no value has been set, the summary displayed will be 'Not set',
     * otherwise the summary displayed will be the entry set for this preference.
     */
    class SimpleSummaryProvider private constructor() :
        SummaryProvider<NumberPickerPreference> {
        override fun provideSummary(preference: NumberPickerPreference): CharSequence? {
            var index = preference.value
            if (index < 0) return null
            val entryValues = preference.displayedValues
            if (entryValues != null) {
                val lastIndex = entryValues.lastIndex
                index = min(lastIndex, index - preference.min)
                return entryValues[index]
            }
            return index.toString()
        }

        companion object {
            /**
             * Retrieve a singleton instance of this simple
             * [androidx.preference.Preference.SummaryProvider] implementation.
             */
            val instance: SimpleSummaryProvider by lazy { SimpleSummaryProvider() }
        }
    }

    companion object {
        private val attrAndroidMin = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            android.R.attr.min
        } else {
            androidx.preference.R.attr.min
        }
    }
}