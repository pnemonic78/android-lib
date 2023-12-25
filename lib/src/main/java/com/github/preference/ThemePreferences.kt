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

import androidx.annotation.StyleRes

/**
 * Theme preferences.
 *
 * @author Moshe Waisberg
 */
interface ThemePreferences {
    object Values {
        /** Default theme. */
        @JvmField
        var THEME_DEFAULT: String? = null

        /** Dark theme. */
        @JvmField
        var THEME_DARK: String? = null

        /** Light theme. */
        @JvmField
        var THEME_LIGHT: String? = null
    }

    /**
     * Get the theme value.
     *
     * @return the theme value.
     */
    val themeValue: String?

    /**
     * Get the theme.
     *
     * @param value the theme value.
     * @return the theme resource id.
     * @see .getThemeValue
     */
    @StyleRes
    fun getTheme(value: String?): Int

    /**
     * Get the theme.
     *
     * @return the theme resource id.
     */
    @get:StyleRes
    val theme: Int

    /**
     * Is the theme dark?
     *
     * @param themeId the theme id.
     * @return `true` if the theme has dark backgrounds and light texts.
     */
    fun isDarkTheme(@StyleRes themeId: Int): Boolean

    /**
     * Is the theme dark?
     *
     * @return `true` if the theme has dark backgrounds and light texts.
     */
    val isDarkTheme: Boolean

    companion object {
        /** Preference name for the theme. */
        const val KEY_THEME = "theme"
    }
}