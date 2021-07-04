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

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceGroup;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreference;

import com.github.lib.R;

import java.util.Calendar;

import timber.log.Timber;

import static android.content.pm.PackageManager.MATCH_DEFAULT_ONLY;

/**
 * This fragment shows the preferences for a header.
 *
 * @author Moshe Waisberg
 */
public abstract class AbstractPreferenceFragment extends PreferenceFragmentCompat implements Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    protected static final String DIALOG_FRAGMENT_TAG = "com.github.preference.DIALOG";

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        final Context context = requireContext();
        int xmlId = getPreferencesXml();
        PreferenceManager.setDefaultValues(context, xmlId, false);
        addPreferencesFromResource(xmlId);
        addChangeListeners(getPreferenceScreen());
    }

    @XmlRes
    protected abstract int getPreferencesXml();

    @Nullable
    protected ListPreference initList(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        Preference preference = findPreference(key);
        if (preference instanceof ListPreference) {
            preference.setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
            return (ListPreference) preference;
        }
        return null;
    }

    @Nullable
    protected RingtonePreference initRingtone(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        Preference preference = findPreference(key);
        if (preference instanceof RingtonePreference) {
            RingtonePreference ring = (RingtonePreference) preference;
            ring.setSummaryProvider(RingtonePreferenceSummaryProvider.getInstance());
            return ring;
        }
        return null;
    }

    @Nullable
    protected TimePreference initTime(String key) {
        if (TextUtils.isEmpty(key)) {
            return null;
        }

        Preference preference = findPreference(key);
        if (preference instanceof TimePreference) {
            TimePreference time = (TimePreference) preference;
            time.setNeutralButtonText(R.string.off);
            time.setSummaryProvider(new TimePreferenceSummaryProvider(preference.getContext()));
            onTimePreferenceChange(time, time.getValue());
            return time;
        }
        return null;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        notifyPreferenceChanged();
        if (preference instanceof SwitchPreference) {
            SwitchPreference checkBox = (SwitchPreference) preference;
            return onCheckBoxPreferenceChange(checkBox, newValue);
        }
        if (preference instanceof TimePreference) {
            TimePreference time = (TimePreference) preference;
            return onTimePreferenceChange(time, newValue);
        }
        return true;
    }

    /**
     * Called when a check box preference has changed its value.
     *
     * @param preference the preference.
     * @param newValue   the new value.
     * @return {@code true} if the user value should be set as the preference value (and persisted).
     */
    protected boolean onCheckBoxPreferenceChange(SwitchPreference preference, Object newValue) {
        return true;
    }

    /**
     * Called when a time preference has probably changed its value.
     * <br>Updates the summary to the new value.
     *
     * @param preference the  preference.
     * @param newValue   the possibly new value.
     */
    protected boolean onTimePreferenceChange(TimePreference preference, Object newValue) {
        if (newValue instanceof Calendar) {
            Calendar value = (Calendar) newValue;
            //Set the value for the summary.
            preference.setTime(value);
        } else {
            String value = (newValue == null) ? null : newValue.toString();
            //Set the value for the summary.
            preference.setTime(value);
        }
        return true;
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        return false;
    }

    /**
     * Notification that the preference has changed and should do something external.
     */
    protected void notifyPreferenceChanged() {
    }

    protected void validateIntent(String key) {
        validateIntent(findPreference(key));
    }

    protected void validateIntent(@Nullable Preference preference) {
        if (preference == null) {
            return;
        }
        final Intent intent = preference.getIntent();
        if (intent == null) {
            return;
        }
        final Context context = preference.getContext();
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, MATCH_DEFAULT_ONLY);
        if (info != null) {
            preference.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    try {
                        context.startActivity(intent);
                    } catch (Exception e) {
                        Timber.e(e, "Error launching intent: %s", intent);
                    }
                    return true;
                }
            });
        } else {
            preference.setIntent(null);
        }
    }

    protected void addChangeListeners(Preference preference) {
        preference.setOnPreferenceChangeListener(this);
        if (preference instanceof PreferenceGroup) {
            addChangeListeners((PreferenceGroup) preference);
        }
    }

    protected void addChangeListeners(PreferenceGroup group) {
        final int count = group.getPreferenceCount();
        for (int i = 0; i < count; i++) {
            addChangeListeners(group.getPreference(i));
        }
    }

    public boolean removePreference(String key) {
        if (TextUtils.isEmpty(key)) {
            return false;
        }

        Preference pref = findPreference(key);
        if (pref != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                return pref.getParent().removePreference(pref);
            }
            return findPreferenceParent(key).removePreference(pref);
        }

        return false;
    }

    @Nullable
    public PreferenceGroup findPreferenceParent(CharSequence key) {
        return findPreferenceParent(key, getPreferenceScreen());
    }

    @Nullable
    protected PreferenceGroup findPreferenceParent(CharSequence key, PreferenceGroup parent) {
        final int preferenceCount = parent.getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            final Preference preference = parent.getPreference(i);
            final String curKey = preference.getKey();

            if (curKey != null && curKey.equals(key)) {
                return parent;
            }

            if (preference instanceof PreferenceGroup) {
                final PreferenceGroup returnedParent = findPreferenceParent(key, (PreferenceGroup) preference);
                if (returnedParent != null) {
                    return returnedParent;
                }
            }
        }

        return null;
    }

    @Nullable
    protected CharSequence findEntry(@NonNull ListPreference preference, String value) {
        int index = preference.findIndexOfValue(value);
        if (index >= 0) {
            return preference.getEntries()[index];
        }
        return null;
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        final FragmentManager fragmentManager = getFragmentManager();
        if (fragmentManager == null) {
            return;
        }
        // check if dialog is already showing
        if (fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return;
        }

        final DialogFragment f;
        if (preference instanceof NumberPickerPreference) {
            f = NumberPreferenceDialog.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        } else if (preference instanceof RingtonePreference) {
            f = RingtonePreferenceDialog.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        } else if (preference instanceof TimePreference) {
            f = TimePreferenceDialog.newInstance(preference.getKey());
            f.setTargetFragment(this, 0);
            f.show(fragmentManager, DIALOG_FRAGMENT_TAG);
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }
}
