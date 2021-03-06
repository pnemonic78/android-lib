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
package com.github.util;

import java.util.Calendar;

import static android.text.format.DateUtils.DAY_IN_MILLIS;

/**
 * Time utilities.
 *
 * @author Moshe Waisberg
 */
public class TimeUtils {

    private TimeUtils() {
    }

    /**
     * Round-up the time.
     *
     * @param time        the time, in milliseconds.
     * @param granularity the granularity, in milliseconds.
     * @return the rounded time.
     */
    public static long roundUp(long time, long granularity) {
        if (time >= 0L) {
            return ((time + (granularity / 4L)) / granularity) * granularity;
        }
        return ((time - (granularity / 4L)) / granularity) * granularity;
    }

    /**
     * Round-down the time.
     *
     * @param time        the time, in milliseconds.
     * @param granularity the granularity, in milliseconds.
     * @return the rounded time.
     */
    public static long roundDown(long time, long granularity) {
        return (time / granularity) * granularity;
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the calendar with the expected day to check against.
     * @param actual   the calendar with the actual day to check.
     * @return {@code true} if the time occurs on the same day.
     */
    public static boolean isSameDay(Calendar expected, Calendar actual) {
        int e1 = expected.get(Calendar.ERA);
        int y1 = expected.get(Calendar.YEAR);
        int m1 = expected.get(Calendar.MONTH);
        int d1 = expected.get(Calendar.DAY_OF_MONTH);

        int e2 = actual.get(Calendar.ERA);
        int y2 = actual.get(Calendar.YEAR);
        int m2 = actual.get(Calendar.MONTH);
        int d2 = actual.get(Calendar.DAY_OF_MONTH);

        return (e1 == e2) && (y1 == y2) && (m1 == m2) && (d1 == d2);
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the calendar with the expected day to check against.
     * @param actual   the actual time to check.
     * @return {@code true} if the time occurs on the same day.
     */
    public static boolean isSameDay(Calendar expected, long actual) {
        final long expectedMillis = expected.getTimeInMillis();
        final int offset = expected.getTimeZone().getOffset(expectedMillis);
        return isSameDay(expectedMillis, actual, offset);
    }

    /**
     * Is the time on the same day?
     *
     * @param expected the time with the expected day to check against.
     * @param actual   the actual time to check.
     * @return {@code true} if the time occurs on the same day.
     */
    public static boolean isSameDay(long expected, long actual) {
        return isSameDay(expected, actual, 0);
    }

    /**
     * Is the time on the same day?
     *
     * @param expected       the time with the expected day to check against.
     * @param actual         the actual time to check.
     * @param timeZoneOffset the time zone offset.
     * @return {@code true} if the time occurs on the same day.
     */
    public static boolean isSameDay(long expected, long actual, int timeZoneOffset) {
        final long midnight1 = roundDown(expected, DAY_IN_MILLIS);
        final long midnight2 = midnight1 + DAY_IN_MILLIS;
        final long actualWithOffset = actual + timeZoneOffset;
        return (midnight1 <= actualWithOffset) && (actualWithOffset < midnight2);
    }
}
