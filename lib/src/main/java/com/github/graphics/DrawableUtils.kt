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
package com.github.graphics

import android.Manifest
import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.WallpaperManager
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.content.PermissionChecker
import com.github.graphics.BitmapUtils.getPixel
import timber.log.Timber

/**
 * Drawable utilities.
 *
 * @author Moshe Waisberg
 */
object DrawableUtils {
    /**
     * Get the dominant color of the wallpaper.
     * Requires [android.Manifest.permission.READ_EXTERNAL_STORAGE] permission since
     * [android.os.Build.VERSION_CODES.M].
     *
     * @param context the context.
     * @return the color - [android.graphics.Color.TRANSPARENT] otherwise.
     */
    @JvmStatic
    fun getWallpaperColor(context: Context): Int {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            return getWallpaperColorPrimary(context)
        }
        if (PermissionChecker.checkCallingOrSelfPermission(
                context,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PermissionChecker.PERMISSION_GRANTED
        ) {
            return getWallpaperColorDrawable(context)
        }
        return Color.TRANSPARENT
    }

    /**
     * Get the dominant color of the wallpaper image.
     * Requires [android.Manifest.permission.READ_EXTERNAL_STORAGE] permission since
     * [android.os.Build.VERSION_CODES.M].
     *
     * @param context the context.
     * @return the color - [android.graphics.Color.TRANSPARENT] otherwise.
     */
    @SuppressLint("InlinedApi")
    @RequiresPermission(
        anyOf = [Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE],
        conditional = true
    )
    fun getWallpaperColorDrawable(context: Context): Int {
        val wallpaperManager = WallpaperManager.getInstance(context)
        var wallpaper: Drawable? = null
        try {
            wallpaper = wallpaperManager.peekDrawable()
            if (wallpaper == null) {
                wallpaper = wallpaperManager.drawable
            }
        } catch (e: Throwable) {
            // In case of a bad WallpaperService.
            Timber.e(e)
        }
        if (wallpaper != null) {
            if (wallpaper is BitmapDrawable) {
                val bm = wallpaper.bitmap
                return getPixel(bm)
            }
            if (wallpaper is ColorDrawable) {
                return wallpaper.color
            }
            val bm = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bm)
            wallpaper.setBounds(0, 0, 1, 1)
            wallpaper.draw(canvas)
            val bg = getPixel(bm)
            bm.recycle()
            return bg
        }
        return Color.TRANSPARENT
    }

    /**
     * Get the dominant color of the wallpaper.
     *
     * @param context the context.
     * @return the color - [android.graphics.Color.TRANSPARENT] otherwise.
     */
    @TargetApi(Build.VERSION_CODES.O_MR1)
    fun getWallpaperColorPrimary(context: Context): Int {
        val wallpaperManager = WallpaperManager.getInstance(context)
        val colors = wallpaperManager.getWallpaperColors(WallpaperManager.FLAG_SYSTEM)
        return colors?.primaryColor?.toArgb() ?: Color.TRANSPARENT
    }
}