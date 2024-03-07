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
package com.github.util

import java.util.Arrays
import java.util.Locale

/**
 * Locale utilities.
 *
 * @author Moshe Waisberg
 */
object LocaleUtils {
    /**
     * ISO 639 language code for "Arabic".
     */
    const val ISO639_ARABIC = "ar"

    /**
     * ISO 639 language code for "Hausa".
     */
    const val ISO639_HAUSA = "ha"

    /**
     * ISO 639 language code for "Hebrew".
     */
    const val ISO639_HEBREW = "he"

    /**
     * ISO 639 language code for "Hebrew" (Java compatibility).
     */
    const val ISO639_HEBREW_JAVA = "iw"

    /**
     * ISO 639 language code for "Yiddish" (Java compatibility).
     */
    const val ISO639_YIDDISH_JAVA = "ji"

    /**
     * ISO 639 language code for "Persian (Farsi)".
     */
    const val ISO639_PERSIAN = "fa"

    /**
     * ISO 639 language code for "Pashto, Pushto".
     */
    const val ISO639_PASHTO = "ps"

    /**
     * ISO 639 language code for "Yiddish".
     */
    const val ISO639_YIDDISH = "yi"

    /**
     * Sort the locales by their display names.
     *
     * @param values the immutable list of locale values.
     * @param locale the display locale.
     * @return the sorted list of locales.
     */
    @JvmStatic
    fun sort(values: Array<String>?, locale: Locale? = null): Array<Locale>? {
        if (values.isNullOrEmpty()) {
            return null
        }
        val locales = Array(values.size) { parseLocale(values[it]) }
        return sortByDisplay(locales, locale)
    }

    /**
     * Sort the locales by their display names.
     *
     * @param locales the list of locales to sort.
     * @param locale  the display locale.
     * @return the sorted list of locales.
     */
    @JvmStatic
    fun sortByDisplay(locales: Array<Locale>?, locale: Locale? = null): Array<Locale>? {
        if (locales.isNullOrEmpty()) {
            return locales
        }
        Arrays.sort(locales, LocaleNameComparator(locale))
        return locales
    }

    @JvmStatic
    fun unique(values: Array<String?>): Array<Locale> {
        val locales = mutableMapOf<String, Locale>()
        for (value in values) {
            val locale = parseLocale(value)
            locales[locale.toString()] = locale
        }
        return locales.values.toTypedArray()
    }
}

/**
 * Is the default locale right-to-left?
 *
 * @return `true` if the locale is RTL.
 */
fun isLocaleRTL(): Boolean = Locale.getDefault().isLocaleRTL()

/**
 * Is the default locale right-to-left?
 *
 * @return `true` if the locale is RTL.
 */
fun Locale.isLocaleRTL(): Boolean {
    return when (language) {
        LocaleUtils.ISO639_ARABIC,
        LocaleUtils.ISO639_HAUSA,
        LocaleUtils.ISO639_HEBREW_JAVA,
        LocaleUtils.ISO639_HEBREW,
        LocaleUtils.ISO639_PASHTO,
        LocaleUtils.ISO639_PERSIAN,
        LocaleUtils.ISO639_YIDDISH,
        LocaleUtils.ISO639_YIDDISH_JAVA -> true
        else -> false
    }
}

/**
 * Parse the locale
 *
 * @param localeValue the locale to parse. For example, `fr` for "French", or `en_UK` for
 * "English (United Kingdom)".
 * @return the locale - empty otherwise.
 */
fun parseLocale(localeValue: String?): Locale {
    return if (localeValue.isNullOrEmpty()) {
        Locale("")
    } else {
        Locale.forLanguageTag(localeValue)
    }
}
