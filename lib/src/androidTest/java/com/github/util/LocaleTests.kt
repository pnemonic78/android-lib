package com.github.util

import com.github.BaseTests
import com.github.lib.test.R
import java.util.Locale
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test

class LocaleTests : BaseTests() {

    private var localeBefore: Locale? = null

    @Before
    fun beforeTest() {
        val localeBefore = context.getDefaultLocale()
        assertNotNull(localeBefore)
        this.localeBefore = localeBefore
    }

    @After
    fun afterTest() {
        val localeBefore = this.localeBefore
        assertNotNull(localeBefore)
        context.applyLocale(localeBefore!!)
    }

    @Test
    fun apply_locale_to_context() {
        val locale = Locale.CHINA
        val contextNew = context.applyLocale(locale)
        assertNotNull(contextNew)
        val localeNew = contextNew.getDefaultLocale()
        assertNotNull(localeNew)
        assertEquals(locale, localeNew)
    }

    @Test
    fun apply_locale_to_context_strings() {
        val localeEnglish = Locale.US
        val contextEnglish = context.applyLocale(localeEnglish)
        assertNotNull(contextEnglish)
        assertEquals(localeEnglish, contextEnglish.getDefaultLocale())
        val helloEnglish = contextEnglish.getString(R.string.hello)
        assertEquals("Hello", helloEnglish)

        val localeHebrew = Locale("he", "IL")
        val contextHebrew = contextEnglish.applyLocale(localeHebrew)
        assertNotNull(contextHebrew)
        assertEquals(localeHebrew, contextHebrew.getDefaultLocale())
        val helloHebrew = contextHebrew.getString(R.string.hello)
        assertEquals("שלום", helloHebrew)
    }
}