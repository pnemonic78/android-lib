package com.github.preference

import androidx.preference.ListPreference
import androidx.preference.Preference

class ListPreferenceSummaryProvider : Preference.SummaryProvider<ListPreference> {
    override fun provideSummary(preference: ListPreference): CharSequence {
        val value = preference.value;
        if (value != null) {
            val values = preference.entryValues
            val entries = preference.entries
            val length = values.size
            for (i in 0 until length) {
                if (value == values[i]) {
                    return entries[i]
                }
            }
        }
        return ""
    }
}