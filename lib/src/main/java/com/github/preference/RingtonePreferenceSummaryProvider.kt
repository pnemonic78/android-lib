package com.github.preference

import androidx.preference.Preference

class RingtonePreferenceSummaryProvider private constructor() :
    Preference.SummaryProvider<RingtonePreference> {

    override fun provideSummary(preference: RingtonePreference): CharSequence {
        return preference.getRingtoneTitle(preference.value)
    }

    companion object {
        private var summaryProvider: RingtonePreferenceSummaryProvider? = null

        /**
         * Retrieve a singleton instance of this simple
         * [androidx.preference.Preference.SummaryProvider] implementation.
         *
         * @return a singleton instance of this simple
         * [androidx.preference.Preference.SummaryProvider] implementation
         */
        fun getInstance(): RingtonePreferenceSummaryProvider {
            if (summaryProvider == null) {
                summaryProvider = RingtonePreferenceSummaryProvider()
            }
            return summaryProvider!!
        }
    }
}