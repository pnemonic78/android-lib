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
package com.github.app

import android.content.Context
import com.github.preference.SimpleThemePreferences
import com.github.preference.ThemePreferences

/**
 * Simple theme callback implementation.
 *
 * @author Moshe Waisberg
 */
open class SimpleThemeCallbacks<TP : ThemePreferences?> @JvmOverloads constructor(
    private val context: Context,
    private var preferences: TP? = null
) : ThemeCallbacks<TP> {

    override fun onPreCreate() {
        context.setTheme(themePreferences!!.theme)
    }

    override val themePreferences: TP
        get() {
            var preferences: TP? = preferences
            if (preferences == null) {
                preferences = createPreferences(context)
                this.preferences = preferences
            }
            return preferences!!
        }

    protected fun createPreferences(context: Context): TP {
        return SimpleThemePreferences(context) as TP
    }
}