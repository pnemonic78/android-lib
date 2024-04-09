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

import android.text.format.DateUtils
import java.util.Calendar
import java.util.TimeZone
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Test times.
 *
 * @author Moshe Waisberg
 */
class TimeTests {
    @Test
    fun roundUpSecond() {
        assertEquals(0L, TimeUtils.roundUp(0, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(111, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(222, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(333, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(444, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(499, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(500, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(555, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(666, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(777, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(888, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(999, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1000, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1111, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1222, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1333, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1444, DateUtils.SECOND_IN_MILLIS))
        assertEquals(1000L, TimeUtils.roundUp(1555, DateUtils.SECOND_IN_MILLIS))
        assertEquals(2000L, TimeUtils.roundUp(1755, DateUtils.SECOND_IN_MILLIS))
    }

    @Test
    fun roundUpNegativeSecond() {
        assertEquals(0L, TimeUtils.roundUp(0, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-111, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-222, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-333, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-444, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-499, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-500, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-555, DateUtils.SECOND_IN_MILLIS))
        assertEquals(0L, TimeUtils.roundUp(-666, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-777, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-888, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-999, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1000, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1111, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1222, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1333, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1444, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-1000L, TimeUtils.roundUp(-1555, DateUtils.SECOND_IN_MILLIS))
        assertEquals(-2000L, TimeUtils.roundUp(-1755, DateUtils.SECOND_IN_MILLIS))
    }

    @Test
    fun sameDay() {
        var time: Long
        var cal: Calendar
        val now = System.currentTimeMillis()
        var timeZone = TimeZone.getTimeZone("UTC")
        assertNotNull(timeZone)
        time = now
        cal = Calendar.getInstance()
        cal.timeZone = timeZone
        cal.timeInMillis = now
        assertTrue(TimeUtils.isSameDay(cal, time))
        cal.timeInMillis = time
        cal.add(Calendar.DAY_OF_MONTH, -1)
        assertFalse(TimeUtils.isSameDay(cal, time))
        cal.timeInMillis = time
        cal.add(Calendar.DAY_OF_MONTH, 1)
        assertFalse(TimeUtils.isSameDay(cal, time))
        cal.timeInMillis = time
        cal.hour = 0
        cal.minute = 59
        cal.second = 59
        time = cal.timeInMillis
        for (j in 0..23) {
            for (i in 0..23) {
                cal.timeInMillis = now
                cal.hour = i
                assertTrue("j=$j,i=$i", TimeUtils.isSameDay(cal, time))
            }
            time += DateUtils.HOUR_IN_MILLIS
        }
        assertFalse(TimeUtils.isSameDay(cal, time))
        timeZone = TimeZone.getTimeZone("Asia/Jerusalem")
        assertNotNull(timeZone)
        val today = 1591116511014L
        cal = Calendar.getInstance(timeZone)
        cal.timeInMillis = today
        assertEquals(2020, cal.year)
        assertEquals(Calendar.JUNE, cal.month)
        assertEquals(2, cal.dayOfMonth)
        assertEquals(19, cal.hour)
        assertEquals(48, cal.minute)
        val midnight1 = TimeUtils.roundDown(today, DateUtils.DAY_IN_MILLIS)
        assertEquals(1591056000000L, midnight1)
        cal = Calendar.getInstance(timeZone)
        cal.timeInMillis = midnight1
        assertEquals(2020, cal.year)
        assertEquals(Calendar.JUNE, cal.month)
        assertEquals(2, cal.dayOfMonth)
        assertEquals(3, cal.hour)
        assertEquals(0, cal.minute)
        assertEquals(0, cal.second)
        assertEquals(0, cal.millisecond)
        time = 1591047482685L //solar midnight
        cal = Calendar.getInstance(timeZone)
        cal.timeInMillis = time
        assertEquals(2020, cal.year)
        assertEquals(Calendar.JUNE, cal.month)
        assertEquals(2, cal.dayOfMonth)
        assertEquals(0, cal.hour)
        assertEquals(38, cal.minute)
        val offset3 = (3 * DateUtils.HOUR_IN_MILLIS).toInt()
        assertEquals(10800000, offset3)
        val offset = timeZone.getOffset(time)
        assertEquals(offset3, offset)
        assertTrue(TimeUtils.isSameDay(today, time, offset))
    }

    @Test
    fun sameDayLevana() {
        val levana: Long
        var cal: Calendar

        // Latest kiddush levana for "2018-09-25 00:18 UTC" => "2018-09-24 20:18 New_York"
        levana = 1537834718503L
        cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("UTC")
        cal.timeInMillis = levana
        assertTrue(TimeUtils.isSameDay(cal, levana))
        cal = Calendar.getInstance()
        cal.timeZone = TimeZone.getTimeZone("America/New_York")
        cal.year = 2018
        cal.month = Calendar.SEPTEMBER
        cal.dayOfMonth = 24
        cal.hour = 9
        cal.minute = 30
        assertTrue(TimeUtils.isSameDay(cal, levana))
    }
}