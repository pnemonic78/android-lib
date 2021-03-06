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
package com.github.preference;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.preference.PreferenceDialogFragmentCompat;

public abstract class PreferenceDialog extends PreferenceDialogFragmentCompat {

    private static final String SAVE_STATE_NEUTRAL_TEXT = "PreferenceDialogFragment.neutralText";

    private CharSequence neutralButtonText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState == null) {
            DialogPreference preference = (DialogPreference) getPreference();
            neutralButtonText = preference.getNeutralButtonText();
        } else {
            neutralButtonText = savedInstanceState.getCharSequence(SAVE_STATE_NEUTRAL_TEXT);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putCharSequence(SAVE_STATE_NEUTRAL_TEXT, neutralButtonText);
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        builder.setNeutralButton(neutralButtonText, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        if (which == DialogInterface.BUTTON_NEUTRAL) {
            DialogPreference preference = (DialogPreference) getPreference();
            preference.onNeutralButtonClicked();
        }
    }
}
