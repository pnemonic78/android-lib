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
package com.github.appwidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import androidx.annotation.IdRes
import timber.log.Timber

/**
 * App widget utilities.
 *
 * @author Moshe Waisberg
 */
object AppWidgetUtils {
    @JvmStatic
    fun getAppWidgetIds(
        context: Context,
        appWidgetClass: Class<out AppWidgetProvider>
    ): IntArray? {
        return getAppWidgetIds(context, null, appWidgetClass)
    }

    @JvmStatic
    fun getAppWidgetIds(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        appWidgetClass: Class<out AppWidgetProvider>
    ): IntArray? {
        return getAppWidgetIds(context, appWidgetManager, getProvider(context, appWidgetClass))
    }

    @JvmStatic
    fun getAppWidgetIds(context: Context, provider: ComponentName): IntArray? {
        return getAppWidgetIds(context, null, provider)
    }

    @JvmStatic
    fun getAppWidgetIds(
        context: Context,
        appWidgetManager: AppWidgetManager?,
        provider: ComponentName
    ): IntArray? {
        var widgetManager = appWidgetManager
        if (widgetManager == null) {
            widgetManager = AppWidgetManager.getInstance(context)
            if (widgetManager == null) {
                Timber.e("no instance of AppWidgetManager!")
                return null
            }
        }
        return widgetManager.getAppWidgetIds(provider)
    }

    @JvmStatic
    fun getProvider(
        context: Context,
        appWidgetClass: Class<out AppWidgetProvider>
    ): ComponentName {
        return ComponentName(context, appWidgetClass)
    }

    @JvmStatic
    fun notifyAppWidgetsUpdate(context: Context, appWidgetClass: Class<out AppWidgetProvider>) {
        val appWidgetIds = getAppWidgetIds(context, appWidgetClass)
        if (appWidgetIds == null || appWidgetIds.isEmpty()) {
            return
        }
        val intent = Intent(context, appWidgetClass)
            .setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE)
            .putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, appWidgetIds)
        context.sendBroadcast(intent)
    }

    @JvmStatic
    fun notifyAppWidgetViewDataChanged(
        context: Context,
        appWidgetClass: Class<out AppWidgetProvider>,
        @IdRes viewId: Int
    ) {
        val appWidgetManager = AppWidgetManager.getInstance(context)
        val appWidgetIds = getAppWidgetIds(context, appWidgetManager, appWidgetClass)
        if (appWidgetIds != null && appWidgetIds.isNotEmpty()) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, viewId)
        }
    }
}