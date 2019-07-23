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
package com.github.preference;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.content.res.TypedArrayUtils;

import com.github.lib.R;

/**
 * Preference that shows a number picker.
 *
 * @author Moshe Waisberg
 */
public class NumberPickerPreference extends DialogPreference {

    private static final int[] ATTRIBUTES = {android.R.attr.max};

    private int value;
    private int max;
    private int min;
    private boolean progressSet;

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        final TypedArray a = context.obtainStyledAttributes(attrs, ATTRIBUTES, defStyleAttr, defStyleRes);
        this.max = a.getInt(0, 100);
        a.recycle();

        if (getDialogLayoutResource() == 0) {
            setDialogLayoutResource(R.layout.preference_dialog_numberpicker);
        }
    }

    public NumberPickerPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public NumberPickerPreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
    }

    public NumberPickerPreference(Context context) {
        this(context, null);
    }

    @Override
    protected Integer onGetDefaultValue(TypedArray a, int index) {
        return a.hasValue(0) ? a.getInt(index, 0) : null;
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setValue(getPersistedInt((defaultValue != null) ? (Integer) defaultValue : value));
    }

    /**
     * Saves the value to the {@link SharedPreferences}.
     *
     * @param value the value.
     */
    public void setValue(int value) {
        // Always persist/notify the first time; don't assume the field's default value.
        final boolean changed = this.value != value;
        if (changed || !progressSet) {
            this.value = value;
            progressSet = true;
            persistInt(value);
            if (changed) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    /**
     * Gets the value from the {@link SharedPreferences}.
     *
     * @return the value.
     */
    public int getValue() {
        return value;
    }

    /**
     * Set the range of the value bar to {@code 0}...{@code max}.
     *
     * @param max the upper range of this value bar.
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * Get the maximum value.
     *
     * @return the upper range of this value bar.
     */
    public int getMax() {
        return max;
    }

    /**
     * Set the range of the value bar to {@code min}...{@code max}.
     *
     * @param min the lower range of this value bar.
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * Get the minimum value.
     *
     * @return the lower range of this value bar.
     */
    public int getMin() {
        return min;
    }
}
