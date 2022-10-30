package com.github.util

import android.content.Context
import android.util.TypedValue

object TypedValueUtils {
    /**
     * @return The resource ID value in the `context` specified by `attr`. If it does
     * not exist, `fallbackAttr`.
     */
    @JvmStatic
    fun getAttr(context: Context, attr: Int, fallbackAttr: Int): Int {
        val value = TypedValue()
        context.theme.resolveAttribute(attr, value, true)
        return if (value.resourceId != 0) attr else fallbackAttr
    }
}