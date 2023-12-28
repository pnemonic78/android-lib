/*
 * Copyright 2018, Moshe Waisberg
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
package com.github.io

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.nio.charset.StandardCharsets
import java.util.Arrays

/**
 * Test streams.
 *
 * @author Moshe Waisberg
 */
class StreamTests {

    @Test
    fun fully() {
        val buf = ByteArray(1000)
        Arrays.fill(buf, 123.toByte())
        val input: InputStream = ByteArrayInputStream(buf)
        val full = StreamUtils.readFully(input)
        assertNotNull(full)
        for (i in 1000 downTo 1) {
            assertEquals(i, full.available())
            assertEquals(123, full.read())
        }
    }

    @Test
    fun stringsUTF8() {
        val sample = "\u0590\u0591\u0000\u0010"
        val buf = sample.toByteArray(StandardCharsets.UTF_8)
        assertEquals((sample.length + 2), buf.size)
        var input: InputStream = ByteArrayInputStream(buf)
        var s = StreamUtils.toString(input, StandardCharsets.UTF_8)
        assertEquals(sample, s)
        input = ByteArrayInputStream(buf)
        s = StreamUtils.toString(input)
        assertEquals(sample, s)
    }

    @Test
    fun stringsISO() {
        val sample = "ABC\t123\r\n"
        val buf = sample.toByteArray(StandardCharsets.UTF_8)
        assertEquals(sample.length, buf.size)
        var input: InputStream = ByteArrayInputStream(buf)
        var s = StreamUtils.toString(input)
        assertEquals(sample, s)
        input = ByteArrayInputStream(buf)
        s = StreamUtils.toString(input, StandardCharsets.UTF_8)
        assertEquals(sample, s)
        input = ByteArrayInputStream(buf)
        s = StreamUtils.toString(input, StandardCharsets.US_ASCII)
        assertEquals(sample, s)
        input = ByteArrayInputStream(buf)
        s = StreamUtils.toString(input, StandardCharsets.ISO_8859_1)
        assertEquals(sample, s)
    }
}