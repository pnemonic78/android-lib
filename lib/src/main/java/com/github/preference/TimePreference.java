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
import android.text.format.DateFormat;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.core.content.res.TypedArrayUtils;

import com.github.lib.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

import static android.text.TextUtils.isEmpty;

/**
 * Preference that shows a time picker.
 * <p>
 * The preference value is stored in the ISO 8601 format {@code hh:mm}
 * </p>
 *
 * @author Moshe Waisberg
 * @see <a href="https://en.wikipedia.org/wiki/ISO_8601#Times">ISO 8601 - Wikipedia</a>
 */
public class TimePreference extends DialogPreference {

    /**
     * ISO 8601 time format.
     */
    private static final String PATTERN = "HH:mm";

    private String value;
    private Calendar time;
    private static final java.text.DateFormat formatIso = new SimpleDateFormat(PATTERN, Locale.US);
    private java.text.DateFormat formatPretty;

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        formatPretty = DateFormat.getTimeFormat(context);

        if (getDialogLayoutResource() == 0) {
            setDialogLayoutResource(R.layout.preference_dialog_timepicker);
        }
    }

    public TimePreference(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public TimePreference(Context context, AttributeSet attrs) {
        this(context, attrs, TypedArrayUtils.getAttr(context, R.attr.dialogPreferenceStyle, android.R.attr.dialogPreferenceStyle));
    }

    public TimePreference(Context context) {
        this(context, null);
    }

    @Override
    protected String onGetDefaultValue(TypedArray a, int index) {
        return a.getString(index);
    }

    @Override
    protected void onSetInitialValue(@Nullable Object defaultValue) {
        setTime((String) defaultValue);
    }

    @Override
    public boolean shouldDisableDependents() {
        return isEmpty(value) || super.shouldDisableDependents();
    }

    /**
     * Saves the time to the {@link SharedPreferences}.
     *
     * @param timeString The chosen time. Can be {@code null}.
     */
    public void setTime(String timeString) {
        final boolean wasBlocking = shouldDisableDependents();

        this.value = timeString;
        this.time = parseTime(timeString);

        if ((time == null) && !isEmpty(timeString)) {
            Timber.e("invalid time [%s] for preference [%s]", timeString, getKey());
        } else {
            persistString(timeString);
        }

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    /**
     * Saves the time to the {@link SharedPreferences}.
     *
     * @param time The chosen time. Can be {@code null}.
     */
    public void setTime(Calendar time) {
        final boolean wasBlocking = shouldDisableDependents();

        String timeString = (time != null) ? formatIso.format(time.getTime()) : null;
        this.value = timeString;
        this.time = time;

        persistString(timeString);

        final boolean isBlocking = shouldDisableDependents();
        if (isBlocking != wasBlocking) {
            notifyDependencyChange(isBlocking);
        }
    }

    /**
     * Gets the time from the {@link SharedPreferences}.
     *
     * @return The current preference value.
     */
    public String getValue() {
        return value;
    }

    /**
     * Gets the time from the {@link SharedPreferences}.
     *
     * @return The current preference value.
     */
    public Calendar getTime() {
        return time;
    }

    /**
     * Format the time as per user's locale.
     *
     * @return the formatted time.
     */
    public String formatTime() {
        return (time != null) ? formatPretty.format(time.getTime()) : null;
    }

    /**
     * Parse the time value.
     *
     * @param timeString the time in ISO 8601 format.
     * @return the time - {@code null} otherwise.
     */
    public static Calendar parseTime(String timeString) {
        if (!isEmpty(timeString)) {
            try {
                Date date = formatIso.parse(timeString);
                Calendar time = Calendar.getInstance();
                time.setTime(date);
                return time;
            } catch (ParseException e) {
                Timber.e(e, "parse time: %s", timeString);
            }
        }
        return null;
    }

    @Override
    public void onNeutralButtonClicked() {
        if (callChangeListener(null)) {
            setTime((Calendar) null);
        }
    }
}
