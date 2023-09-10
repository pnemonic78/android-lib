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
import androidx.preference.PreferenceDialogFragmentCompat

abstract class PreferenceDialog : PreferenceDialogFragmentCompat() {

    private var neutralButtonText: CharSequence? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        neutralButtonText = if (savedInstanceState == null) {
            val preference = preference as DialogPreference
            preference.neutralButtonText
        } else {
            savedInstanceState.getCharSequence(SAVE_STATE_NEUTRAL_TEXT)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putCharSequence(SAVE_STATE_NEUTRAL_TEXT, neutralButtonText)
    }

    override fun onPrepareDialogBuilder(builder: AlertDialog.Builder) {
        super.onPrepareDialogBuilder(builder)
        builder.setNeutralButton(neutralButtonText, this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        super.onClick(dialog, which)
        if (which == DialogInterface.BUTTON_NEUTRAL) {
            val preference = preference as DialogPreference
            preference.onNeutralButtonClicked()
        }
    }

    companion object {
        private const val SAVE_STATE_NEUTRAL_TEXT = "PreferenceDialogFragment.neutralText"
    }
}