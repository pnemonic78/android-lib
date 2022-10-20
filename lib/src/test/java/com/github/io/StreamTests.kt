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

import com.github.nio.charset.StandardCharsets
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import java.io.ByteArrayInputStream
import java.io.InputStream
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
        val `in`: InputStream = ByteArrayInputStream(buf)
        val full = StreamUtils.readFully(`in`)
        assertNotNull(full)
        for (i in 1000 downTo 1) {
            assertEquals(i.toLong(), full.available().toLong())
            assertEquals(123, full.read().toLong())
        }
    }

    @Test
    fun stringsUTF8() {
        val sample = "\u0590\u0591\u0000\u0010"
        val buf = sample.toByteArray(StandardCharsets.UTF_8)
        assertEquals((sample.length + 2).toLong(), buf.size.toLong())
        var `in`: InputStream = ByteArrayInputStream(buf)
        var s = StreamUtils.toString(`in`, StandardCharsets.UTF_8)
        assertEquals(sample, s)
        `in` = ByteArrayInputStream(buf)
        s = StreamUtils.toString(`in`)
        assertEquals(sample, s)
    }

    @Test
    fun stringsISO() {
        val sample = "ABC\t123\r\n"
        val buf = sample.toByteArray(StandardCharsets.UTF_8)
        assertEquals(sample.length.toLong(), buf.size.toLong())
        var `in`: InputStream = ByteArrayInputStream(buf)
        var s = StreamUtils.toString(`in`)
        assertEquals(sample, s)
        `in` = ByteArrayInputStream(buf)
        s = StreamUtils.toString(`in`, StandardCharsets.UTF_8)
        assertEquals(sample, s)
        `in` = ByteArrayInputStream(buf)
        s = StreamUtils.toString(`in`, StandardCharsets.US_ASCII)
        assertEquals(sample, s)
        `in` = ByteArrayInputStream(buf)
        s = StreamUtils.toString(`in`, StandardCharsets.ISO_8859_1)
        assertEquals(sample, s)
    }
}