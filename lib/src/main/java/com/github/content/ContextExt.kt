package com.github.content

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.PluralsRes

val Context.isNightMode: Boolean
    get() {
        val nightMode = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return (nightMode == Configuration.UI_MODE_NIGHT_YES)
    }

fun Context.getQuantityString(@PluralsRes id: Int, quantity: Int): String =
    resources.getQuantityString(id, quantity)

fun Context.getQuantityString(
    @PluralsRes id: Int,
    quantity: Int,
    vararg formatArgs: Any?
): String = resources.getQuantityString(id, quantity, *formatArgs)
