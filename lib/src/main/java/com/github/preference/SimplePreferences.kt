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
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.github.content.ProviderPreferences

/**
 * Application settings.
 *
 * @author Moshe Waisberg
 */
open class SimplePreferences @JvmOverloads constructor(
    @JvmField
    protected val context: Context,
    multiProcess: Boolean = false
) {
    /**
     * Get the preferences.
     *
     * @return the shared preferences.
     */
    @JvmField
    val preferences: SharedPreferences = if (multiProcess)
        ProviderPreferences(context)
    else
        PreferenceManager.getDefaultSharedPreferences(context)
}