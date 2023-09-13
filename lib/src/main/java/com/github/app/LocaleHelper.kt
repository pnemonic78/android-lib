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

import android.app.Activity
import android.content.Context
import com.github.app.ActivityUtils.resetTitle
import com.github.preference.LocalePreferences
import com.github.preference.SimpleLocalePreferences
import com.github.util.LocaleUtils

/**
 * Wraps a callback delegate.
 *
 * @author Moshe Waisberg
 */
class LocaleHelper<P : LocalePreferences?>(context: Context) : LocaleCallbacks<P> {
    override val localePreferences: P

    init {
        localePreferences = SimpleLocalePreferences(context) as P
    }

    override fun attachBaseContext(context: Context): Context {
        return LocaleUtils.applyLocale(context, localePreferences!!.locale)
    }

    override fun onPreCreate(context: Context) {
        if (context is Activity) {
            onCreate(context)
        }
    }

    protected fun onCreate(activity: Activity) {
        resetTitle(activity)
    }
}