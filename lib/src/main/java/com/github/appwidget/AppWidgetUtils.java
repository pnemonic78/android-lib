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
package com.github.appwidget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import timber.log.Timber;

import static android.appwidget.AppWidgetManager.ACTION_APPWIDGET_UPDATE;
import static android.appwidget.AppWidgetManager.EXTRA_APPWIDGET_IDS;

/**
 * App widget utilities.
 *
 * @author Moshe Waisberg
 */
public class AppWidgetUtils {

    private AppWidgetUtils() {
    }

    public static int[] getAppWidgetIds(@NonNull Context context, @NonNull Class<? extends AppWidgetProvider> appWidgetClass) {
        return getAppWidgetIds(context, null, appWidgetClass);
    }

    public static int[] getAppWidgetIds(@NonNull Context context, @Nullable AppWidgetManager appWidgetManager, @NonNull Class<? extends AppWidgetProvider> appWidgetClass) {
        return getAppWidgetIds(context, appWidgetManager, getProvider(context, appWidgetClass));
    }

    public static int[] getAppWidgetIds(@NonNull Context context, ComponentName provider) {
        return getAppWidgetIds(context, null, provider);
    }

    public static int[] getAppWidgetIds(@NonNull Context context, @Nullable AppWidgetManager appWidgetManager, @NonNull ComponentName provider) {
        if (appWidgetManager == null) {
            appWidgetManager = AppWidgetManager.getInstance(context);
            if (appWidgetManager == null) {
                Timber.e("no instance of AppWidgetManager!");
                return null;
            }
        }
        return appWidgetManager.getAppWidgetIds(provider);
    }

    public static ComponentName getProvider(@NonNull Context context, @NonNull Class<? extends AppWidgetProvider> appWidgetClass) {
        return new ComponentName(context, appWidgetClass);
    }

    public static void notifyAppWidgetsUpdate(@NonNull Context context, @NonNull Class<? extends AppWidgetProvider> appWidgetClass) {
        int[] appWidgetIds = getAppWidgetIds(context, appWidgetClass);
        if ((appWidgetIds == null) || (appWidgetIds.length == 0)) {
            return;
        }

        Intent intent = new Intent(context, appWidgetClass);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);
    }

    public static void notifyAppWidgetViewDataChanged(Context context, @NonNull Class<? extends AppWidgetProvider> appWidgetClass, int viewId) {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        int[] appWidgetIds = getAppWidgetIds(context, appWidgetManager, appWidgetClass);
        if ((appWidgetIds != null) && (appWidgetIds.length > 0)) {
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, viewId);
        }
    }
}
