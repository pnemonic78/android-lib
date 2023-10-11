package com.github.os

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.VibrationAttributes
import androidx.annotation.RequiresPermission

class VibratorCompat(context: Context) {

    private val delegate: VibratorImpl =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Vibrator33(context)
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Vibrator26(context)
        } else {
            Vibrator1(context)
        }

    fun hasVibrator(): Boolean {
        return delegate.hasVibrator()
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(durationMillis: Long, usage: Int = USAGE_UNKNOWN) {
        delegate.vibrate(durationMillis, usage)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun vibrate(pattern: LongArray, usage: Int = USAGE_UNKNOWN) {
        delegate.vibrate(pattern, usage)
    }

    @RequiresPermission(Manifest.permission.VIBRATE)
    fun cancel() {
        delegate.cancel()
    }

    companion object {
        /**
         * Usage value to use when usage is unknown.
         */
        const val USAGE_UNKNOWN = VibrationAttributes.USAGE_UNKNOWN

        /**
         * Usage value to use for alarm vibrations.
         */
        const val USAGE_ALARM = VibrationAttributes.USAGE_ALARM

        /**
         * Usage value to use for ringtone vibrations.
         */
        const val USAGE_RINGTONE = VibrationAttributes.USAGE_RINGTONE

        /**
         * Usage value to use for notification vibrations.
         */
        const val USAGE_NOTIFICATION = VibrationAttributes.USAGE_NOTIFICATION

        /**
         * Usage value to use for vibrations which mean a request to enter/end a
         * communication with the user, such as a voice prompt.
         */
        const val USAGE_COMMUNICATION_REQUEST = VibrationAttributes.USAGE_COMMUNICATION_REQUEST

        /**
         * Usage value to use for touch vibrations.
         *
         *
         * Most typical haptic feedback should be classed as *touch* feedback. Examples
         * include vibrations for tap, long press, drag and scroll.
         */
        const val USAGE_TOUCH = VibrationAttributes.USAGE_TOUCH

        /**
         * Usage value to use for vibrations which emulate physical hardware reactions,
         * such as edge squeeze.
         *
         *
         * Note that normal screen-touch feedback "click" effects would typically be
         * classed as [.USAGE_TOUCH], and that on-screen "physical" animations
         * like bouncing would be [.USAGE_MEDIA].
         */
        const val USAGE_PHYSICAL_EMULATION = VibrationAttributes.USAGE_PHYSICAL_EMULATION

        /**
         * Usage value to use for vibrations which provide a feedback for hardware
         * component interaction, such as a fingerprint sensor.
         */
        const val USAGE_HARDWARE_FEEDBACK = VibrationAttributes.USAGE_HARDWARE_FEEDBACK

        /**
         * Usage value to use for accessibility vibrations, such as with a screen reader.
         */
        const val USAGE_ACCESSIBILITY = VibrationAttributes.USAGE_ACCESSIBILITY

        /**
         * Usage value to use for media vibrations, such as music, movie, soundtrack, animations, games,
         * or any interactive media that isn't for touch feedback specifically.
         */
        const val USAGE_MEDIA = VibrationAttributes.USAGE_MEDIA
    }
}