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

import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import androidx.annotation.ChecksSdkIntAtLeast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.github.lib.R

/**
 * Application preferences that populate the settings.
 *
 * @author Moshe Waisberg
 */
abstract class PreferenceActivity : AppCompatActivity(), OnSharedPreferenceChangeListener,
    PreferenceFragmentCompat.OnPreferenceStartFragmentCallback {

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.O)
    private var restartParentActivityForUi = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
        setContentView(R.layout.preference_activity)
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings_container, createMainFragment())
            .commit()
    }

    protected abstract fun createMainFragment(): PreferenceFragmentCompat

    override fun onDestroy() {
        super.onDestroy()
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun finish() {
        // Recreate the parent activity in case a theme has changed.
        var parentIntent = parentActivityIntent
        if (parentIntent == null) {
            try {
                val pm = packageManager
                val info = pm.getActivityInfo(componentName, 0)
                val parentActivity = info.parentActivityName
                parentIntent = Intent()
                    .setClassName(this, parentActivity)
            } catch (ignore: PackageManager.NameNotFoundException) {
            }
        }
        if (parentIntent != null && shouldUpRecreateTask(parentIntent)) {
            parentIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(parentIntent)
        }
        super.finish()
    }

    override fun shouldUpRecreateTask(targetIntent: Intent): Boolean {
        return restartParentActivityForUi || super.shouldUpRecreateTask(targetIntent)
    }

    private fun markRestartParentActivityForUi() {
        restartParentActivityForUi = true
    }

    protected open fun shouldRestartParentActivityForUi(key: String?): Boolean {
        return false
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (shouldRestartParentActivityForUi(key)) {
            markRestartParentActivityForUi()
        }
    }

    override fun onPreferenceStartFragment(
        caller: PreferenceFragmentCompat,
        pref: Preference
    ): Boolean {
        return false
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (supportFragmentManager.backStackEntryCount > 0) {
                supportFragmentManager.popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}