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
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;

public class TimePreferenceDialog extends PreferenceDialog {

    public static TimePreferenceDialog newInstance(String key) {
        final TimePreferenceDialog dialog = new TimePreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        dialog.setArguments(b);
        return dialog;
    }

    private TimePicker picker;

    @Override
    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);

        final Context context = view.getContext();

        picker = view.findViewById(android.R.id.edit);
        if (picker == null) {
            throw new IllegalStateException("Dialog view must contain a TimePicker with id 'edit'");
        }
        picker.setIs24HourView(DateFormat.is24HourFormat(context));

        TimePreference preference = getTimePreference();
        Calendar time = preference.getTime();
        if (time != null) {
            picker.setCurrentHour(time.get(Calendar.HOUR_OF_DAY));
            picker.setCurrentMinute(time.get(Calendar.MINUTE));
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        if (positiveResult) {
            Calendar time = Calendar.getInstance();
            time.set(Calendar.HOUR_OF_DAY, picker.getCurrentHour());
            time.set(Calendar.MINUTE, picker.getCurrentMinute());

            TimePreference preference = getTimePreference();

            if (preference.callChangeListener(time)) {
                preference.setTime(time);
            }
        }
    }

    private TimePreference getTimePreference() {
        return (TimePreference) getPreference();
    }
}
