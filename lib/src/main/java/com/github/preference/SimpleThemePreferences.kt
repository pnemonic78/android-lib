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
import android.content.res.Configuration
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
            THEME_DARK -> androidx.appcompat.R.style.Theme_AppCompat
            THEME_LIGHT -> androidx.appcompat.R.style.Theme_AppCompat_Light
            else -> androidx.appcompat.R.style.Theme_AppCompat_DayNight
        }
    }

    override val theme: Int
        get() = getTheme(themeValue)

    override fun isDarkTheme(value: String?): Boolean {
        if (THEME_LIGHT == value) {
            return false
        }
        if (THEME_DEFAULT == value) {
            val nightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
            return when (nightMode) {
                Configuration.UI_MODE_NIGHT_NO ->  false
                Configuration.UI_MODE_NIGHT_YES ->  true
                // Material
                else -> false
            }
        }
        return true
    }

    override val isDarkTheme: Boolean
        get() = isDarkTheme(themeValue)
}