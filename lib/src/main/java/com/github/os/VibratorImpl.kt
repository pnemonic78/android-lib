package com.github.os

import android.Manifest
import android.os.Vibrator
import androidx.annotation.RequiresPermission

internal abstract class VibratorImpl {

    protected abstract val vibrator: Vibrator?

    fun hasVibrator(): Boolean {
        return vibrator?.hasVibrator() ?: false
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(durationMillis: Long, usage: Int = VibratorCompat.USAGE_UNKNOWN) {
        val vibrator = this.vibrator ?: return
        if (!vibrator.hasVibrator()) return
        vibrate(vibrator, durationMillis, usage)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    protected abstract fun vibrate(vibrator: Vibrator, durationMillis: Long, usage: Int)

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(pattern: LongArray, usage: Int = VibratorCompat.USAGE_UNKNOWN) {
        val vibrator = this.vibrator ?: return
        if (!vibrator.hasVibrator()) return
        vibrate(vibrator, pattern, usage)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    protected abstract fun vibrate(vibrator: Vibrator, pattern: LongArray, usage: Int)

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun cancel() {
        vibrator?.cancel()
    }
}