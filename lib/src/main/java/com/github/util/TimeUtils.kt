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

/**
 * Time utilities.
 *
 * @author Moshe Waisberg
 */
object TimeUtils {
    /**
     * Round-up the time.
     *
     * @param time        the time, in milliseconds.
     * @param granularity the granularity, in milliseconds.
     * @return the rounded time.
     */
    @JvmStatic
    fun roundUp(time: Long, granularity: Long): Long {
        return if (time >= 0L) {
            ((time + (granularity / 4L)) / granularity) * granularity
        } else {
            ((time - (granularity / 4L)) / granularity) * granularity
        }
    }

    /**
     * Round-down the time.
     *
     * @param time        the time, in milliseconds.
     * @param granularity the granularity, in milliseconds.
     * @return the rounded time.
     */
    @JvmStatic
    fun roundDown(time: Long, granularity: Long): Long {
        return (time / granularity) * granularity
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the calendar with the expected day to check against.
     * @param actual   the calendar with the actual day to check.
     * @return `true` if the time occurs on the same day.
     */
    @JvmStatic
    fun isSameDay(expected: Calendar, actual: Calendar): Boolean {
        val e1 = expected[Calendar.ERA]
        val y1 = expected[Calendar.YEAR]
        val m1 = expected[Calendar.MONTH]
        val d1 = expected[Calendar.DAY_OF_MONTH]

        val e2 = actual[Calendar.ERA]
        val y2 = actual[Calendar.YEAR]
        val m2 = actual[Calendar.MONTH]
        val d2 = actual[Calendar.DAY_OF_MONTH]

        return (e1 == e2) && (y1 == y2) && (m1 == m2) && (d1 == d2)
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the calendar with the expected day to check against.
     * @param actual   the actual time to check.
     * @return `true` if the time occurs on the same day.
     */
    @JvmStatic
    fun isSameDay(expected: Calendar, actual: Long): Boolean {
        val expectedMillis = expected.timeInMillis
        val offset = expected.timeZone.getOffset(expectedMillis)
        return isSameDay(expectedMillis, actual, offset)
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the time with the expected day to check against.
     * @param actual   the actual time to check.
     * @return `true` if the time occurs on the same day.
     */
    @JvmStatic
    fun isSameDay(expected: Long, actual: Long): Boolean {
        return isSameDay(expected, actual, 0)
    }

    /**
     * Is the time on the same day?
     *
     * @param expected       the time with the expected day to check against.
     * @param actual         the actual time to check.
     * @param timeZoneOffset the time zone offset.
     * @return `true` if the time occurs on the same day.
     */
    @JvmStatic
    fun isSameDay(expected: Long, actual: Long, timeZoneOffset: Int): Boolean {
        val midnight1 = roundDown(expected, DateUtils.DAY_IN_MILLIS)
        val midnight2 = midnight1 + DateUtils.DAY_IN_MILLIS
        val actualWithOffset = actual + timeZoneOffset
        return actualWithOffset in midnight1 until midnight2
    }
}