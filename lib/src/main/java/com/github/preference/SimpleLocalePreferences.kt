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
import android.content.res.Resources
import com.github.preference.LocalePreferences.Companion.KEY_LOCALE
import com.github.util.getDefaultLocale
import com.github.util.parseLocale
import java.util.Locale

/**
 * Locale preferences implementation.
 *
 * @author Moshe Waisberg
 */
open class SimpleLocalePreferences : SimplePreferences, LocalePreferences {
    constructor(context: Context) : this(context, false)

    constructor(context: Context, multiProcess: Boolean) : super(context, multiProcess)

    override val locale: Locale
        get() {
            val value = preferences.getString(KEY_LOCALE, null)
            return if (value.isNullOrEmpty()) {
                Resources.getSystem().getDefaultLocale()
            } else {
                parseLocale(value)
            }
        }
}