package com.github.util

import android.content.Context
import android.content.res.TypedArray
import android.util.TypedValue
import androidx.annotation.AttrRes
import androidx.annotation.StyleableRes

object TypedValueUtils {
    /**
     * @return The resource ID value in the `context` specified by `attr`. If it does
     * not exist, `fallbackAttr`.
     */
    @JvmStatic
    @AttrRes
    fun getAttr(context: Context, attr: Int, @AttrRes fallbackAttr: Int): Int {
        return context.getAttr(attr, fallbackAttr)
    }

    /**
     * Retrieves a string array attribute value with the specified fallback ID.
     *
     * @return a string array value of `index`. If it does not exist, a string array value
     * of `fallbackIndex`. If it still does not exist, `null`.
     */
    @JvmStatic
    fun getTextArray(
        a: TypedArray,
        @StyleableRes index: Int,
        @StyleableRes fallbackIndex: Int
    ): Array<CharSequence?>? {
        var arr: Array<CharSequence?>? = a.getTextArray(index)
        if (arr == null) {
            arr = a.getTextArray(fallbackIndex)
        }
        return arr
    }

    /**
     * Retrieves a string array attribute value with the specified fallback ID.
     *
     * @return a string array value of `index`. If it does not exist, a string array value
     * of `fallbackIndex`. If it still does not exist, `null`.
     */
    @JvmStatic
    fun getStringArray(
        a: TypedArray,
        @StyleableRes index: Int,
        @StyleableRes fallbackIndex: Int
    ): Array<String?>? {
        return getTextArray(a, index, fallbackIndex)?.map { it?.toString() }?.toTypedArray()
    }
}

/**
 * @return The resource ID value in the `context` specified by `attr`. If it does
 * not exist, `fallbackAttr`.
 */
@AttrRes
fun Context.getAttr(attr: Int, @AttrRes fallbackAttr: Int): Int {
    val value = TypedValue()
    this.theme.resolveAttribute(attr, value, true)
    return if (value.resourceId != 0) attr else fallbackAttr
}