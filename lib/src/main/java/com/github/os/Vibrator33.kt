package com.github.os

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.annotation.RequiresPermission

@TargetApi(Build.VERSION_CODES.TIRAMISU)
internal class Vibrator33(context: Context) : Vibrator26(context) {

    override val vibrator: Vibrator? by lazy {
        val vibratorManager = context.getSystemService(VibratorManager::class.java)
        vibratorManager?.defaultVibrator
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(vibrator: Vibrator, durationMillis: Long, usage: Int) {
        val vibe = VibrationEffect.createOneShot(
            durationMillis,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
        val attributes = VibrationAttributes.createForUsage(usage)
        vibrator.vibrate(vibe, attributes)
    }
}