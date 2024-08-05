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
import java.util.Locale

/**
 * Apply the default locale.
 *
 * @param locale  the locale to set.
 * @return the context with the applied locale.
 */
fun Context.applyLocale(locale: Locale): Context {
    Locale.setDefault(locale)
    val res = resources ?: Resources.getSystem()!!
    val config = res.configuration
    config.setLocale(locale)
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
        @Suppress("DEPRECATION")
        res.updateConfiguration(config, res.displayMetrics)
    }
    return createConfigurationContext(config)
}

/**
 * Is the locale right-to-left?
 *
 * @return `true` if the locale is RTL.
 */
fun Context.isLocaleRTL(): Boolean {
    return resources.isLocaleRTL()
}

/**
 * Is the locale right-to-left?
 *
 * @return `true` if the locale is RTL.
 */
fun Resources.isLocaleRTL(): Boolean {
    return configuration.isLocaleRTL()
}

/**
 * Is the locale right-to-left?
 *
 * @return `true` if the locale is RTL.
 */
fun Configuration.isLocaleRTL(): Boolean {
    return getDefaultLocale().isLocaleRTL()
}

/**
 * Get the default locale.
 *
 * @return the locale.
 */
fun Context.getDefaultLocale(): Locale {
    return resources.getDefaultLocale()
}

/**
 * Get the default locale.
 *
 * @return the locale.
 */
@TargetApi(Build.VERSION_CODES.N)
fun Configuration.getDefaultLocale(): Locale {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        return locales.getDefaultLocale()
    }
    @Suppress("DEPRECATION")
    return locale ?: Locale.getDefault()
}

/**
 * Get the default locale.
 *
 * @return the locale.
 */
fun Resources.getDefaultLocale(): Locale {
    return configuration.getDefaultLocale()
}

/**
 * Get the default locale.
 *
 * @return the locale.
 */
@TargetApi(Build.VERSION_CODES.N)
fun LocaleList.getDefaultLocale(): Locale {
    var locales = this
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
 * Sort the locales by their display names.
 *
 * @param values  the immutable list of locale values.
 * @param context the display context.
 * @return the sorted list of locale names.
 */
fun sortLocales(values: Array<String>?, context: Context): Array<Locale>? {
    return LocaleUtils.sort(values, context.getDefaultLocale())
}
