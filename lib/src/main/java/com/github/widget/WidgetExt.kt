package com.github.widget

import android.os.Build
import android.widget.TimePicker

@Suppress("DEPRECATION")
var TimePicker.hours: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.hour
    } else {
        this.currentHour
    }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.hour = value
        } else {
            this.currentHour = value
        }
    }

@Suppress("DEPRECATION")
var TimePicker.minutes: Int
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        this.hour
    } else {
        this.currentHour
    }
    set(value) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.hour = value
        } else {
            this.currentHour = value
        }
    }