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

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import com.github.lib.R;

import java.util.List;

public class RingtonePreferenceDialog extends PreferenceDialog {

    private static final String SAVE_STATE_SELECTED = "RingtonePreferenceDialog.selected";

    private CharSequence[] entries;
    private int selectedIndex = -1;

    public static RingtonePreferenceDialog newInstance(String key) {
        final RingtonePreferenceDialog dialog = new RingtonePreferenceDialog();
        final Bundle b = new Bundle(1);
        b.putString(ARG_KEY, key);
        dialog.setArguments(b);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RingtonePreference preference = getRingtonePreference();

        List<CharSequence> entriesList = preference.getEntries();
        entries = entriesList.toArray(new CharSequence[0]);

        if (savedInstanceState == null) {
            selectedIndex = preference.getValueIndex();
        } else {
            selectedIndex = savedInstanceState.getInt(SAVE_STATE_SELECTED, selectedIndex);
        }
    }

    @Override
    protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
        super.onPrepareDialogBuilder(builder);

        builder.setSingleChoiceItems(entries, selectedIndex, this);
        builder.setPositiveButton(R.string.ok, this);
        builder.setNegativeButton(R.string.cancel, this);
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        super.onClick(dialog, which);

        if (which >= 0) {
            RingtonePreference preference = getRingtonePreference();
            // Play clip
            preference.playRingtone(which);
            selectedIndex = which;
        }
    }

    @Override
    public void onDialogClosed(boolean positiveResult) {
        RingtonePreference preference = getRingtonePreference();
        preference.stopAnyPlayingRingtone();

        if (positiveResult && (selectedIndex >= 0)) {
            Uri uri = preference.getRingtoneUri(selectedIndex);
            if (preference.callChangeListener(uri != null ? uri.toString() : RingtonePreference.SILENT_PATH)) {
                preference.onSaveRingtone(uri);
            }
        }
    }

    private RingtonePreference getRingtonePreference() {
        return (RingtonePreference) getPreference();
    }
}
