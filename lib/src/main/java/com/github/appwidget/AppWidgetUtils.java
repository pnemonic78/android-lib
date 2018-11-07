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

    public static int[] getAppWidgetIds(Context context, Class<? extends AppWidgetProvider> appWidgetClass) {
        return getAppWidgetIds(context, null, appWidgetClass);
    }

    public static int[] getAppWidgetIds(Context context, AppWidgetManager appWidgetManager, Class<? extends AppWidgetProvider> appWidgetClass) {
        return getAppWidgetIds(context, appWidgetManager, getProvider(context, appWidgetClass));
    }

    public static int[] getAppWidgetIds(Context context, ComponentName provider) {
        return getAppWidgetIds(context, null, provider);
    }

    public static int[] getAppWidgetIds(Context context, AppWidgetManager appWidgetManager, ComponentName provider) {
        if (appWidgetManager == null) {
            appWidgetManager = AppWidgetManager.getInstance(context);
        }
        return appWidgetManager.getAppWidgetIds(provider);
    }

    public static ComponentName getProvider(Context context, Class<? extends AppWidgetProvider> appWidgetClass) {
        return new ComponentName(context, appWidgetClass);
    }

    public static void notifyAppWidgetsUpdate(Context context, Class<? extends AppWidgetProvider> appWidgetClass) {
        int[] appWidgetIds = getAppWidgetIds(context, appWidgetClass);
        if ((appWidgetIds == null) || (appWidgetIds.length == 0)) {
            return;
        }

        Intent intent = new Intent(context, appWidgetClass);
        intent.setAction(ACTION_APPWIDGET_UPDATE);
        intent.putExtra(EXTRA_APPWIDGET_IDS, appWidgetIds);
        context.sendBroadcast(intent);
    }

}
