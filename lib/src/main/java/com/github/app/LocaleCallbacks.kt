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
import com.github.preference.LocalePreferences

/**
 * Contexts that overrides the system locale.
 *
 * @author Moshe Waisberg
 */
interface LocaleCallbacks<P : LocalePreferences> {
    /**
     * Apply the locale here.
     *
     * @param context the context with locale.
     * @return the context with the new locale.
     */
    fun attachBaseContext(context: Context): Context

    /**
     * Re-apply the texts here.
     *
     * @param context the context.
     */
    fun onPreCreate(context: Context)

    /**
     * Get the locale preferences.
     *
     * @return the preferences.
     */
    val localePreferences: P
}