package com.github.os

import android.Manifest
import android.content.Context
import android.os.Vibrator
import androidx.annotation.RequiresPermission

@Suppress("DEPRECATION")
internal open class Vibrator1(context: Context) : VibratorImpl() {

    override val vibrator: Vibrator? =
        context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(vibrator: Vibrator, durationMillis: Long, usage: Int) {
        vibrator.vibrate(durationMillis)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(vibrator: Vibrator, pattern: LongArray, usage: Int) {
        vibrator.vibrate(pattern, 0)
    }
}