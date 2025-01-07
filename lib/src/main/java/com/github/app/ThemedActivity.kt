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
package com.github.app

import android.os.Bundle
import com.github.preference.ThemePreferences

/**
 * Activity that takes its theme from the application.
 *
 * @author Moshe Waisberg
 */
@Deprecated("")
abstract class ThemedActivity<P : ThemePreferences> : InsetsActivity(), ThemeCallbacks<P> {

    protected val themeCallbacks: ThemeCallbacks<P> = SimpleThemeCallbacks(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        onPreCreate()
        super.onCreate(savedInstanceState)
    }

    override fun onPreCreate() {
        themeCallbacks.onPreCreate()
    }

    override val themePreferences: P
        get() = themeCallbacks.themePreferences
}