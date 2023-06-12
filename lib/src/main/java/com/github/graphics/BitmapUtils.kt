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
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import androidx.annotation.RequiresPermission

/**
 * Bitmap utilities.
 *
 * @author Moshe Waisberg
 */
object BitmapUtils {
    /**
     * Get the dominant color of the image.
     *
     * @param bm the bitmap.
     * @return the color - `{ android.graphics.Color#TRANSPARENT}` otherwise.
     */
    @JvmStatic
    fun getPixel(bm: Bitmap): Int {
        val pixel = if (bm.width <= 8 && bm.height <= 8)
            bm
        else
            Bitmap.createScaledBitmap(bm, 1, 1, true)
        if (!pixel.isRecycled) {
            val bg = pixel.getPixel(0, 0)
            if (bm != pixel) {
                pixel.recycle()
            }
            return bg
        }
        return Color.TRANSPARENT
    }

    /**
     * Is the color bright?
     *
     * Useful for determining whether to use dark color on bright background.
     *
     * @param color the color.
     * @return `true` if the color is "bright".
     */
    @JvmStatic
    fun isBright(color: Int): Boolean {
        val a = Color.alpha(color)
        if (a >= 0x80) {
            val r = Color.red(color) >= 0xB0
            val g = Color.green(color) >= 0xB0
            val b = Color.blue(color) >= 0xB0
            return r && g || r && b || g && b
        }
        return false
    }

    /**
     * Is the wallpaper bright?
     *
     * Useful for determining whether to use dark color on bright background.
     *
     * @param context the context.
     * @return `true` if the wallpaper is "bright".
     */
    @JvmStatic
    @RequiresPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
    fun isBrightWallpaper(context: Context): Boolean {
        return isBright(DrawableUtils.getWallpaperColor(context))
    }
}

fun Bitmap.getPixel(): Int = BitmapUtils.getPixel(this)