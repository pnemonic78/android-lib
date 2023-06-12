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

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
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
    const val ISO639_HEBREW_FORMER = "he"

    /**
     * ISO 639 language code for "Hebrew" (Java compatibility).
     */
    const val ISO639_HEBREW = "iw"

    /**
     * ISO 639 language code for "Yiddish" (Java compatibility).
     */
    const val ISO639_YIDDISH_FORMER = "ji"

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
     * Is the default locale right-to-left?
     *
     * @return `true` if the locale is RTL.
     */
    @JvmStatic
    fun isLocaleRTL(): Boolean = isLocaleRTL(Locale.getDefault())

    /**
     * Is the locale right-to-left?
     *
     * @param context the context.
     * @return `true` if the locale is RTL.
     */
    @JvmStatic
    fun isLocaleRTL(context: Context): Boolean {
        return isLocaleRTL(context.resources)
    }

    /**
     * Is the locale right-to-left?
     *
     * @param res the resources.
     * @return `true` if the locale is RTL.
     */
    @JvmStatic
    fun isLocaleRTL(res: Resources): Boolean {
        return isLocaleRTL(res.configuration)
    }

    /**
     * Is the locale right-to-left?
     *
     * @param config the configuration.
     * @return `true` if the locale is RTL.
     */
    @JvmStatic
    fun isLocaleRTL(config: Configuration): Boolean {
        return isLocaleRTL(getDefaultLocale(config))
    }

    /**
     * Is the default locale right-to-left?
     *
     * @return `true` if the locale is RTL.
     */
    @JvmStatic
    fun isLocaleRTL(locale: Locale): Boolean {
        return when (locale.language) {
            ISO639_ARABIC,
            ISO639_HAUSA,
            ISO639_HEBREW,
            ISO639_HEBREW_FORMER,
            ISO639_PASHTO,
            ISO639_PERSIAN,
            ISO639_YIDDISH,
            ISO639_YIDDISH_FORMER -> true
            else -> false
        }
    }

    /**
     * Get the default locale.
     *
     * @param context the context.
     * @return the locale.
     */
    @JvmStatic
    fun getDefaultLocale(context: Context): Locale {
        return getDefaultLocale(context.resources)
    }

    /**
     * Get the default locale.
     *
     * @param config the configuration with locales.
     * @return the locale.
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.N)
    fun getDefaultLocale(config: Configuration): Locale {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return getDefaultLocale(config.locales)
        }
        return config.locale ?: Locale.getDefault()
    }

    /**
     * Get the default locale.
     *
     * @param res the resources.
     * @return the locale.
     */
    @JvmStatic
    fun getDefaultLocale(res: Resources): Locale {
        return getDefaultLocale(res.configuration)
    }

    /**
     * Get the default locale.
     *
     * @param localesList the list of locales.
     * @return the locale.
     */
    @JvmStatic
    @TargetApi(Build.VERSION_CODES.N)
    fun getDefaultLocale(localesList: LocaleList): Locale {
        var locales = localesList
        if (locales.isEmpty) {
            locales = LocaleList.getAdjustedDefault()
        }
        var locale: Locale? = null
        if (!locales.isEmpty) {
            locale = locales[0]
        }
        return locale ?: Locale.getDefault()
    }

    /**
     * Apply the default locale.
     *
     * @param context the context.
     * @param locale  the locale to set.
     * @return the context with the applied locale.
     */
    @JvmStatic
    fun applyLocale(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val res = context.resources ?: Resources.getSystem()!!
        val config = res.configuration
        config.setLocale(locale)
        res.updateConfiguration(config, res.displayMetrics)
        return context.createConfigurationContext(config)
    }

    /**
     * Sort the locales by their display names.
     *
     * @param values  the immutable list of locale values.
     * @param context the display context.
     * @return the sorted list of locale names.
     */
    @JvmStatic
    fun sort(values: Array<String>?, context: Context): Array<Locale>? {
        return sort(values, getDefaultLocale(context))
    }

    /**
     * Sort the locales by their display names.
     *
     * @param values the immutable list of locale values.
     * @param locale the display locale.
     * @return the sorted list of locales.
     */
    @JvmStatic
    @JvmOverloads
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
    @JvmOverloads
    fun sortByDisplay(locales: Array<Locale>?, locale: Locale? = null): Array<Locale>? {
        if (locales.isNullOrEmpty()) {
            return locales
        }
        Arrays.sort(locales, LocaleNameComparator(locale))
        return locales
    }

    /**
     * Parse the locale
     *
     * @param localeValue the locale to parse. For example, `fr` for "French", or `en_UK` for
     * "English (United Kingdom)".
     * @return the locale - empty otherwise.
     */
    @JvmStatic
    fun parseLocale(localeValue: String?): Locale {
        return if (localeValue.isNullOrEmpty()) {
            Locale("")
        } else {
            Locale.forLanguageTag(localeValue)
        }
    }

    @JvmStatic
    fun unique(values: Array<String?>): Array<Locale> {
        val locales: MutableMap<String, Locale> = mutableMapOf()
        for (value in values) {
            val locale = parseLocale(value)
            locales[locale.toString()] = locale
        }
        return locales.values.toTypedArray()
    }
}