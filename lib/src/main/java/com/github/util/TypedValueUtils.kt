package com.github.util

import android.content.Context
import android.util.TypedValue
import androidx.annotation.IdRes

object TypedValueUtils {
    /**
     * @return The resource ID value in the `context` specified by `attr`. If it does
     * not exist, `fallbackAttr`.
     */
    @JvmStatic
    @IdRes
    fun getAttr(context: Context, attr: Int, fallbackAttr: Int): Int {
        return context.getAttr(attr, fallbackAttr)
    }
}

/**
 * @return The resource ID value in the `context` specified by `attr`. If it does
 * not exist, `fallbackAttr`.
 */
@IdRes
fun Context.getAttr(attr: Int, fallbackAttr: Int): Int {
    val value = TypedValue()
    this.theme.resolveAttribute(attr, value, true)
    return if (value.resourceId != 0) attr else fallbackAttr
}