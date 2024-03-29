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

import android.os.Build
import android.os.Bundle
import androidx.annotation.XmlRes
import androidx.fragment.app.DialogFragment
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceGroup
import androidx.preference.PreferenceManager
import androidx.preference.TwoStatePreference
import com.github.lib.R
import java.util.Calendar
import timber.log.Timber

/**
 * This fragment shows the preferences for a header.
 *
 * @author Moshe Waisberg
 */
abstract class AbstractPreferenceFragment : PreferenceFragmentCompat(),
    Preference.OnPreferenceChangeListener, Preference.OnPreferenceClickListener {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        loadPreferences()
        addChangeListeners(preferenceScreen)
    }

    private fun loadPreferences() {
        val context = requireContext()
        val xmlId = preferencesXml
        PreferenceManager.setDefaultValues(context, xmlId, false)
        addPreferencesFromResource(xmlId)
    }

    @get:XmlRes
    protected abstract val preferencesXml: Int

    protected inline fun <reified P : RingtonePreference> initRingtone(key: String?): P? {
        if (key.isNullOrEmpty()) {
            return null
        }
        val preference = findPreferenceLenient<P>(key) ?: return null
        return initRingtone(preference)
    }

    protected fun <P : RingtonePreference> initRingtone(preference: P): P {
        preference.summaryProvider = RingtonePreferenceSummaryProvider.getInstance()
        return preference
    }

    protected fun initTime(key: String?): TimePreference? {
        if (key.isNullOrEmpty()) {
            return null
        }
        val preference = findPreferenceLenient<TimePreference>(key) ?: return null
        return initTime(preference)
    }

    protected fun initTime(preference: TimePreference): TimePreference {
        preference.setNeutralButtonText(R.string.off)
        onTimePreferenceChange(preference, preference.value)
        return preference
    }

    protected fun initNumber(key: String?): NumberPickerPreference? {
        if (key.isNullOrEmpty()) {
            return null
        }
        val preference = findPreferenceLenient<NumberPickerPreference>(key) ?: return null
        return initNumber(preference)
    }

    protected fun initNumber(preference: NumberPickerPreference): NumberPickerPreference {
        preference.setNeutralButtonText(R.string.off)
        return preference
    }

    override fun onPreferenceChange(preference: Preference, newValue: Any?): Boolean {
        notifyPreferenceChanged(preference)
        if (preference is TwoStatePreference) {
            return onCheckBoxPreferenceChange(preference, newValue)
        }
        if (preference is TimePreference) {
            return onTimePreferenceChange(preference, newValue)
        }
        return true
    }

    /**
     * Called when a check box preference has changed its value.
     *
     * @param preference the preference.
     * @param newValue   the new value.
     * @return `true` if the user value should be set as the preference value (and persisted).
     */
    protected open fun onCheckBoxPreferenceChange(
        preference: TwoStatePreference,
        newValue: Any?
    ): Boolean {
        return true
    }

    /**
     * Called when a time preference has probably changed its value.
     * <br></br>Updates the summary to the new value.
     *
     * @param preference the  preference.
     * @param newValue   the possibly new value.
     */
    protected open fun onTimePreferenceChange(preference: TimePreference, newValue: Any?): Boolean {
        if (newValue is Calendar) {
            //Set the value for the summary.
            preference.setTime(newValue)
        } else {
            //Set the value for the summary.
            preference.setTime(newValue?.toString())
        }
        return true
    }

    override fun onPreferenceClick(preference: Preference): Boolean {
        return false
    }

    /**
     * Notification that the preference has changed and should do something external.
     */
    protected open fun notifyPreferenceChanged(preference: Preference) = Unit

    protected fun validateIntent(key: String) {
        validateIntent(findPreference(key))
    }

    protected fun validateIntent(preference: Preference?) {
        if (preference == null) {
            return
        }
        val intent = preference.intent ?: return
        val context = preference.context
        val pm = context.packageManager
        val name = intent.resolveActivity(pm)
        if (name != null) {
            preference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
                try {
                    context.startActivity(intent)
                } catch (e: Exception) {
                    Timber.e(e, "Error launching intent: %s", intent)
                }
                true
            }
        } else {
            preference.intent = null
        }
    }

    private fun addChangeListeners(preference: Preference) {
        preference.onPreferenceChangeListener = this
        if (preference is PreferenceGroup) {
            addChangeListeners(preference)
        }
    }

    private fun addChangeListeners(group: PreferenceGroup) {
        val count = group.preferenceCount
        for (i in 0 until count) {
            addChangeListeners(group.getPreference(i))
        }
    }

    fun removePreference(key: String?): Boolean {
        if (key.isNullOrEmpty()) {
            return false
        }
        val pref = findPreference<Preference>(key)
        return if (pref != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                pref.parent?.removePreference(pref) ?: false
            } else {
                findPreferenceParent(key)?.removePreference(pref) ?: false
            }
        } else {
            false
        }
    }

    private fun findPreferenceParent(key: CharSequence): PreferenceGroup? {
        return findPreferenceParent(key, preferenceScreen)
    }

    private fun findPreferenceParent(
        key: CharSequence,
        parent: PreferenceGroup
    ): PreferenceGroup? {
        val preferenceCount = parent.preferenceCount
        for (i in 0 until preferenceCount) {
            val preference = parent.getPreference(i)
            val curKey = preference.key
            if (curKey == key) {
                return parent
            }
            if (preference is PreferenceGroup) {
                val returnedParent = findPreferenceParent(key, preference)
                if (returnedParent != null) {
                    return returnedParent
                }
            }
        }
        return null
    }

    protected fun findEntry(preference: ListPreference, value: String?): CharSequence? {
        val index = preference.findIndexOfValue(value)
        return if (index >= 0) preference.entries[index] else null
    }

    // FIXME `setTargetFragment` still required, until "androidx.preference:preference-ktx"
    //  fixes "Target fragment must implement TargetFragment interface" !
    override fun onDisplayPreferenceDialog(preference: Preference) {
        val fragmentManager = parentFragmentManager
        // check if dialog is already showing
        if (fragmentManager.findFragmentByTag(DIALOG_FRAGMENT_TAG) != null) {
            return
        }
        val key = preference.key
        val f: DialogFragment
        when (preference) {
            is NumberPickerPreference -> {
                f = NumberPreferenceDialog.newInstance(key)
                f.setTargetFragment(this, 0)
                f.show(fragmentManager, DIALOG_FRAGMENT_TAG)
            }

            is RingtonePreference -> {
                f = RingtonePreferenceDialog.newInstance(key)
                f.setTargetFragment(this, 0)
                f.show(fragmentManager, DIALOG_FRAGMENT_TAG)
            }

            is TimePreference -> {
                f = TimePreferenceDialog.newInstance(key)
                f.setTargetFragment(this, 0)
                f.show(fragmentManager, DIALOG_FRAGMENT_TAG)
            }

            else -> super.onDisplayPreferenceDialog(preference)
        }
    }

    protected inline fun <reified T : Preference> findPreferenceLenient(key: CharSequence): T? {
        val preference = findPreference<Preference>(key) ?: return null
        return (preference as? T)
    }

    companion object {
        const val DIALOG_FRAGMENT_TAG = "com.github.preference.DIALOG"
    }
}