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
package com.github.view

import android.content.Context
import android.os.Build
import android.view.Display
import android.view.View
import android.view.ViewParent
import android.view.WindowManager
import kotlin.math.max

/**
 * View utilities.
 *
 * @author Moshe Waisberg
 */
object ViewUtils {
    /**
     * Make all views at least the same width.
     *
     * @param views the list of views.
     */
    @JvmStatic
    fun applyMaxWidth(views: Array<View?>) {
        var maxWidth = 0
        // First, calculate the maximum widths of the views.
        for (view in views) {
            maxWidth = max(maxWidth, view?.measuredWidth ?: 0)
        }
        // Then, apply the maximum width to all the views.
        for (view in views) {
            view?.minimumWidth = maxWidth
        }
    }
}

fun View.findDisplay(): Display {
    var display = this.display
    if (display != null) return display

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        display = context.display
        if (display != null) return display
    }

    var parent: ViewParent? = this.parent
    while (parent != null) {
        if (parent is View) {
            return parent.findDisplay()
        }
        parent = parent.parent
    }

    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    @Suppress("DEPRECATION")
    return windowManager.defaultDisplay
}