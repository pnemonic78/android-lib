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
package com.github.preference

import android.content.Context
import androidx.annotation.StyleRes
import com.github.content.isNightMode
import com.github.lib.R
import com.github.preference.ThemePreferences.Values.THEME_DARK
import com.github.preference.ThemePreferences.Values.THEME_DEFAULT
import com.github.preference.ThemePreferences.Values.THEME_LIGHT

/**
 * Simple theme preferences implementation.
 *
 * @author Moshe Waisberg
 */
open class SimpleThemePreferences(context: Context) : SimplePreferences(context), ThemePreferences {

    init {
        val res = context.resources
        THEME_DEFAULT = res.getString(R.string.theme_value_default)
        THEME_DARK = res.getString(R.string.theme_value_dark)
        THEME_LIGHT = res.getString(R.string.theme_value_light)
    }

    override val themeValue: String?
        get() = preferences.getString(ThemePreferences.KEY_THEME, THEME_DEFAULT)

    override fun getTheme(value: String?): Int {
        return when (value) {
            THEME_DARK -> ThemeDark
            THEME_LIGHT -> ThemeLight
            else -> ThemeDayNight
        }
    }

    override val theme: Int
        get() = getTheme(themeValue)

    override fun isDarkTheme(@StyleRes themeId: Int): Boolean {
        return when (themeId) {
            ThemeDark -> true
            ThemeLight -> false
            else -> context.isNightMode
        }
    }

    override val isDarkTheme: Boolean
        get() = isDarkTheme(theme)

    companion object {
        @StyleRes
        private val ThemeDark = androidx.appcompat.R.style.Theme_AppCompat

        @StyleRes
        private val ThemeLight = androidx.appcompat.R.style.Theme_AppCompat_Light

        @StyleRes
        private val ThemeDayNight = androidx.appcompat.R.style.Theme_AppCompat_DayNight
    }
}