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

    @JvmStatic
    fun compareTo(s1: String?, s2: String?, ignoreCase: Boolean = false): Int {
        if (s1 == s2) return 0
        if (s1 == null) return -1
        if (s2 == null) return +1
        return s1.compareTo(s2, ignoreCase = ignoreCase)
    }
}
