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
import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import com.github.app.ActivityUtils.resetTitle
import com.github.preference.LocalePreferences
import com.github.preference.SimpleLocalePreferences
import com.github.util.LocaleUtils

/**
 * Wraps a callback delegate.
 *
 * @author Moshe Waisberg
 */
class LocaleHelper<P : LocalePreferences>(context: Context) : LocaleCallbacks<P> {
    @Suppress("UNCHECKED_CAST")
    override val localePreferences: P = SimpleLocalePreferences(context) as P

    override fun attachBaseContext(context: Context): Context {
        return LocaleUtils.applyLocale(context, localePreferences.locale)
    }

    override fun onPreCreate(context: Context) {
        if (context is Activity) {
            onCreate(context)
        }
    }

    private fun onCreate(activity: Activity) {
        resetTitle(activity)

        val context: Context = activity
        val application = context.applicationContext
        if (application is Application) {

        }
    }

    private class LocalReceiver(private val application: Application) : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (ACTION_LOCALE_CHANGED == action) {
                var contextForLocale: Context = application
                val localeValue = intent.getStringExtra(EXTRA_LOCALE)
                val locale = LocaleUtils.parseLocale(localeValue)
                contextForLocale = LocaleUtils.applyLocale(contextForLocale, locale)
                val newConfig = contextForLocale.resources.configuration
                application.onConfigurationChanged(newConfig)
            }
        }
    }

    companion object {
        const val ACTION_LOCALE_CHANGED = "com.github.android.LOCALE_CHANGED"
        const val EXTRA_LOCALE = "locale"

        private var receiver: LocalReceiver? = null

        @JvmStatic
        fun registerReceiver(application: Application) {
            val context: Context = application
            var receiver = receiver
            if (receiver == null) {
                receiver = LocalReceiver(application)
                LocaleHelper.receiver = receiver
                val intentFilter = IntentFilter(ACTION_LOCALE_CHANGED)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.registerReceiver(receiver, intentFilter, Context.RECEIVER_NOT_EXPORTED)
                } else {
                    context.registerReceiver(receiver, intentFilter)
                }
            }
        }

        @JvmStatic
        fun sendLocaleChanged(context: Context, newLocale: String) {
            val intent = Intent(ACTION_LOCALE_CHANGED)
                .setPackage(context.packageName)
                .putExtra(EXTRA_LOCALE, newLocale)
            context.sendBroadcast(intent)
        }
    }
}