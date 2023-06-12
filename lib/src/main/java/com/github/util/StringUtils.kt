package com.github.util

/**
 * String utilities.
 *
 * @author Moshe Waisberg
 */
object StringUtils {
    @JvmStatic
    fun toString(s: CharSequence?): String? {
        return s?.toString()
    }
}
