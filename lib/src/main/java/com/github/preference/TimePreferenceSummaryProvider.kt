package com.github.preference

import android.content.Context
import android.text.format.DateFormat
import androidx.preference.Preference
import com.github.lib.R

class TimePreferenceSummaryProvider(private val context: Context) :
    Preference.SummaryProvider<TimePreference> {

    private val formatPretty = DateFormat.getTimeFormat(context)

    override fun provideSummary(preference: TimePreference): CharSequence {
        val time = preference.time ?: return context.getString(R.string.off)
        return formatPretty.format(time.time)
    }
}