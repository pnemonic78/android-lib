package com.github.os

import android.Manifest
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import androidx.annotation.RequiresPermission

@TargetApi(Build.VERSION_CODES.O)
internal open class Vibrator26(context: Context) : Vibrator1(context) {

    override val vibrator: Vibrator? = context.getSystemService(Vibrator::class.java)

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(vibrator: Vibrator, durationMillis: Long, usage: Int) {
        val vibe = VibrationEffect.createOneShot(
            durationMillis,
            VibrationEffect.DEFAULT_AMPLITUDE
        )
        vibrator.vibrate(vibe)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    override fun vibrate(vibrator: Vibrator, pattern: LongArray, usage: Int) {
        val vibe = VibrationEffect.createWaveform(pattern, 0)
        vibrator.vibrate(vibe)
    }
}