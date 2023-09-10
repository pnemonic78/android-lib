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
import android.content.ComponentName
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.GET_META_DATA
import android.os.Bundle
import android.os.PersistableBundle
import kotlin.math.min
import timber.log.Timber

/**
 * Activity utilities.
 *
 * @author Moshe Waisberg
 */
object ActivityUtils {
    /**
     * Reset the title.
     *
     * @param activity the activity with a title.
     */
    @JvmStatic
    fun resetTitle(activity: Activity) {
        val name: ComponentName = activity.componentName
        try {
            val label = activity.packageManager.getActivityInfo(name, GET_META_DATA).labelRes
            if (label != 0) {
                activity.setTitle(label)
            }
        } catch (e: PackageManager.NameNotFoundException) {
            Timber.e(e, "package not found for %s", name)
        }
    }

    /**
     * Restart the activity.
     *
     * @param activity   the activity.
     * @param savedState saved state from either [Activity.onSaveInstanceState]
     * or [Activity.onSaveInstanceState].
     */
    @JvmStatic
    fun restartActivity(activity: Activity, savedState: Bundle?) {
        val intent: Intent = activity.intent
        val extras: Bundle? = intent.extras
        val args: Bundle = savedState ?: Bundle()
        if (extras != null) {
            args.putAll(extras)
        }
        intent.putExtras(args)
        activity.finish()
        activity.startActivity(intent)
    }

    /**
     * Restart the activity.
     *
     * @param activity the activity.
     */
    @JvmStatic
    fun restartActivity(activity: Activity) {
        val savedState = Bundle()
        val outPersistentState = PersistableBundle()
        activity.onSaveInstanceState(savedState, outPersistentState)
        restartActivity(activity, savedState)
    }

    @JvmStatic
    fun isPermissionGranted(
        permission: String,
        permissions: Array<String>,
        grantResults: IntArray
    ): Boolean {
        val length = min(permissions.size, grantResults.size)
        for (i in 0 until length) {
            if (permission == permissions[i] && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                return true
            }
        }
        return false
    }
}

fun Activity.resetTitle() = ActivityUtils.resetTitle(this)

fun Activity.restart(savedState: Bundle?) = ActivityUtils.restartActivity(this, savedState)

fun Activity.restart() = ActivityUtils.restartActivity(this)

