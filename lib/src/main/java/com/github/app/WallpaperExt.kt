package com.github.app

import android.Manifest
import android.content.Context
import com.github.graphics.BitmapUtils
import com.github.graphics.DrawableUtils

const val PERMISSION_WALLPAPER = Manifest.permission.READ_EXTERNAL_STORAGE

/**
 * Is the wallpaper bright?
 *
 * Useful for determining whether to use dark color on bright background.
 *
 * @param context the context.
 * @return `true` if the wallpaper is "bright".
 */
fun isBrightWallpaper(context: Context): Boolean {
    return BitmapUtils.isBright(DrawableUtils.getWallpaperColor(context))
}
