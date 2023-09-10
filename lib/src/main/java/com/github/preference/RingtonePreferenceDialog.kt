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

import android.content.DialogInterface
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.github.lib.R

open class RingtonePreferenceDialog : PreferenceDialog() {

    private var entries: Array<CharSequence?> = emptyArray()

    private var selectedIndex = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val preference = ringtonePreference
        entries = preference.getEntries().toTypedArray()
        selectedIndex = savedInstanceState?.getInt(SAVE_STATE_SELECTED, selectedIndex) ?: preference.valueIndex
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        super.onPrepareDialogBuilder(builder)
        builder.setSingleChoiceItems(entries, selectedIndex, this)
            .setPositiveButton(R.string.ok, this)
            .setNegativeButton(R.string.cancel, this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        super.onClick(dialog, which)
        if (which >= 0) {
            val preference = ringtonePreference
            // Play clip
            preference.playRingtone(which)
            selectedIndex = which
        }
    }

    override fun onDialogClosed(positiveResult: Boolean) {
        val preference = ringtonePreference
        preference.stopAnyPlayingRingtone()
        if (positiveResult && selectedIndex >= 0) {
            val uri = preference.getRingtoneUri(selectedIndex)
            if (preference.callChangeListener(uri?.toString() ?: RingtonePreference.SILENT_PATH)) {
                preference.setValue(uri)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(SAVE_STATE_SELECTED, selectedIndex)
    }

    private val ringtonePreference: RingtonePreference
        get() = preference as RingtonePreference

    companion object {
        private const val SAVE_STATE_SELECTED = "RingtonePreferenceDialog.selected"

        fun newInstance(key: String): RingtonePreferenceDialog {
            return RingtonePreferenceDialog().apply {
                arguments = Bundle(1).apply {
                    putString(ARG_KEY, key)
                }
            }
        }
    }
}