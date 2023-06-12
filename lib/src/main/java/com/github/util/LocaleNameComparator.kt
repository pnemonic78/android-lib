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
package com.github.util

import java.util.Locale

/**
 * Locale name comparator.
 *
 * @author Moshe Waisberg
 */
class LocaleNameComparator @JvmOverloads constructor(private val locale: Locale? = null) :
    Comparator<Locale> {
    override fun compare(lhs: Locale, rhs: Locale): Int {
        val name1 = lhs.getDisplayName(locale ?: lhs)
        val name2 = rhs.getDisplayName(locale ?: rhs)
        return name1.compareTo(name2)
    }
}