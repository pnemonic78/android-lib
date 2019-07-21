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

import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

public class NumberPreferenceDialog extends PreferenceDialog {

    public static NumberPreferenceDialog newInstance(String key) {
        final NumberPreferenceDialog dialog = new NumberPreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        dialog.setArguments(b);
        return dialog;
    }

    private NumberPicker picker;

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        picker = view.findViewById(android.R.id.edit);
        if (picker == null) {
            throw new IllegalStateException("Dialog view must contain a NumberPicker with id 'edit'");
        }

        NumberPickerPreference preference = getNumberPickerPreference();
        picker.setMinValue(preference.getMin());
        picker.setMaxValue(preference.getMax());
        picker.setValue(preference.getValue());
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            picker.clearFocus();
            int value = picker.getValue();

            NumberPickerPreference preference = getNumberPickerPreference();
            if (preference.callChangeListener(value)) {
                preference.setValue(value);
            }
        }
    }

    private NumberPickerPreference getNumberPickerPreference() {
        return (NumberPickerPreference) getPreference();
    }
}
