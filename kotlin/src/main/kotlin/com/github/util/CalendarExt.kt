package com.github.util

import java.util.Calendar

var Calendar.era: Int
    get() = this[Calendar.ERA]
    set(value) {
        this[Calendar.ERA] = value
    }

var Calendar.year: Int
    get() = this[Calendar.YEAR]
    set(value) {
        this[Calendar.YEAR] = value
    }

var Calendar.month: Int
    get() = this[Calendar.MONTH]
    set(value) {
        this[Calendar.MONTH] = value
    }

var Calendar.dayOfMonth: Int
    get() = this[Calendar.DAY_OF_MONTH]
    set(value) {
        this[Calendar.DAY_OF_MONTH] = value
    }

var Calendar.hour: Int
    get() = this[Calendar.HOUR_OF_DAY]
    set(value) {
        this[Calendar.HOUR_OF_DAY] = value
    }

var Calendar.minute: Int
    get() = this[Calendar.MINUTE]
    set(value) {
        this[Calendar.MINUTE] = value
    }

var Calendar.second: Int
    get() = this[Calendar.SECOND]
    set(value) {
        this[Calendar.SECOND] = value
    }

var Calendar.millisecond: Int
    get() = this[Calendar.MILLISECOND]
    set(value) {
        this[Calendar.MILLISECOND] = value
    }
