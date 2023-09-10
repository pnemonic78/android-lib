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
package com.github.backup

import android.app.backup.BackupAgentHelper
import android.app.backup.SharedPreferencesBackupHelper
import android.content.Context

/**
 * Backup agent helper for the application for shared preferences.
 */
open class SharedPreferencesBackupAgent : BackupAgentHelper() {
    override fun onCreate() {
        super.onCreate()
        val prefsName = getDefaultSharedPreferencesName(this)
        val helper = SharedPreferencesBackupHelper(this, prefsName)
        addHelper(PREFS_BACKUP_KEY, helper)
    }

    companion object {
        /** A key to uniquely identify the set of backup data.  */
        private const val PREFS_BACKUP_KEY = "prefs"

        /** Copied from android.preference.PreferenceManager  */
        private fun getDefaultSharedPreferencesName(context: Context): String {
            return context.packageName + "_preferences"
        }
    }
}