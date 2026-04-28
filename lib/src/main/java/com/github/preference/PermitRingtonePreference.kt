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
package com.github.preference

import android.content.Context
import android.util.AttributeSet
import androidx.activity.ComponentActivity
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.github.lang.BooleanCallback
import com.github.lang.VoidCallback
import com.github.media.RingtoneManager
import com.github.util.TypedValueUtils

/**
 * A [androidx.preference.Preference] that allows the user to choose a ringtone from those on the device.
 * The chosen ringtone's URI will be persisted as a string.
 * Requests a user's permissions to read external media.
 *
 * @author Moshe Waisberg
 */
class PermitRingtonePreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    @AttrRes defStyleAttr: Int = TypedValueUtils.getAttr(
        context,
        androidx.preference.R.attr.dialogPreferenceStyle,
        android.R.attr.ringtonePreferenceStyle
    ),
    @StyleRes defStyleRes: Int = 0
) : RingtonePreference(context, attrs, defStyleAttr, defStyleRes) {
    private var requestPermissionLauncher: ActivityResultLauncher<String>? = null

    override fun onClick() {
        val launcher = requestPermissionLauncher
        if (launcher != null) {
            launcher.launch(PERMISSION_RINGTONE)
            return
        }
        super.onClick()
    }

    fun markRequestPermissions(owner: Fragment) {
        val callback = ActivityResultCallback { isGranted: Boolean ->
            val launcher = requestPermissionLauncher ?: return@ActivityResultCallback

            if (isGranted) {
                // Don't ask again.
                requestPermissionLauncher = null
                launcher.unregister()

                setRingtoneType(ringtoneType, true)
                // Show the list.
                onClick()
            } else {
                // Permission denied: handle accordingly
                if (owner.shouldShowRequestPermissionRationale(PERMISSION_RINGTONE)) {
                    showRationaleDialog(context, title) {
                        launcher.launch(PERMISSION_RINGTONE)
                    }
                } else {
                    // Don't ask again.
                    requestPermissionLauncher = null
                    // Show the list anyway.
                    onClick()
                }
            }
        }
        requestPermissionLauncher = owner.registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
            callback
        )
    }

    companion object {
        val PERMISSION_RINGTONE = RingtoneManager.PERMISSION_RINGTONE

        fun askPermission(
            caller: ComponentActivity,
            message: CharSequence? = null,
            callback: BooleanCallback? = null
        ) {
            val context: Context = caller
            var launcher: ActivityResultLauncher<String>? = null
            val callback = ActivityResultCallback { isGranted: Boolean ->
                if (isGranted) {
                    launcher!!.unregister()
                    callback?.invoke(true)
                } else {
                    // Permission denied: handle accordingly
                    if (caller.shouldShowRequestPermissionRationale(PERMISSION_RINGTONE)) {
                        showRationaleDialog(context, caller.title, message) {
                            launcher!!.launch(PERMISSION_RINGTONE)
                        }
                    } else {
                        callback?.invoke(false)
                    }
                }
            }

            launcher = caller.registerForActivityResult(
                ActivityResultContracts.RequestPermission(),
                callback
            )
            launcher.launch(PERMISSION_RINGTONE)
        }

        fun showRationaleDialog(
            context: Context,
            title: CharSequence? = null,
            message: CharSequence? = null,
            callback: VoidCallback
        ) {
            AlertDialog.Builder(context)
                .setTitle(title ?: "Ringtone Permission")
                .setMessage(
                    message ?: "App needs permission to play ringtone audio from external files."
                )
                .setOnDismissListener { callback() }
                .setPositiveButton(android.R.string.ok) { _, _ -> }
                .show()
        }
    }
}