/*
 * Copyright 2024, Moshe Waisberg
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
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test

/**
 * Test locales.
 *
 * @author Moshe Waisberg
 */
class LocaleTests {
    @Test
    fun parsing_English() {
        val locale = parseLocale("en")
        assertNotNull(locale)
        assertEquals(Locale.ENGLISH, locale)
    }

    @Test
    fun parsing_Chinese() {
        val locale = parseLocale("zh")
        assertNotNull(locale)
        assertEquals(Locale.CHINESE, locale)

        val localeWithCountry = parseLocale("zh_CN")
        assertNotNull(localeWithCountry)
        assertEquals(Locale.SIMPLIFIED_CHINESE, localeWithCountry)
        assertEquals("zh_CN", localeWithCountry.toString())
        assertEquals("zh-CN", localeWithCountry.toLanguageTag())
    }
}