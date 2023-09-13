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
package com.github.io

import java.io.BufferedInputStream
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.io.Reader
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import kotlin.math.max

/**
 * Stream utilities.
 *
 * @author Moshe Waisberg
 */
object StreamUtils {

    private const val BUFFER_SIZE = 1024

    /**
     * Read all the bytes from the input stream.
     *
     * @param in the input.
     * @return the array of bytes.
     * @throws IOException if an I/O error occurs.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun readFully(`in`: InputStream): InputStream {
        var stream = `in`
        stream = BufferedInputStream(stream)
        return readFully(stream, stream.available())
    }

    /**
     * Read all the bytes from the input stream.
     *
     * @param `in`   the input.
     * @param size the initial buffer size.
     * @return the array of bytes.
     * @throws IOException if an I/O error occurs.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun readFully(`in`: InputStream, size: Int): InputStream {
        val stream = `in`
        val bufSize = max(size, BUFFER_SIZE)
        val out = RawByteArrayOutputStream(bufSize)
        val buf = ByteArray(BUFFER_SIZE)
        var count: Int
        while (stream.read(buf).also { count = it } >= 0) {
            out.write(buf, 0, count)
        }
        out.close()
        return ByteArrayInputStream(out.byteArray, 0, out.size())
    }

    /**
     * Convert the stream bytes to a sequence of characters.
     *
     * @param in the input.
     * @return the characters.
     * @throws IOException if an I/O error occurs.
     */
    @JvmOverloads
    @JvmStatic
    @Throws(IOException::class)
    fun toCharSequence(
        `in`: InputStream,
        charset: Charset = StandardCharsets.UTF_8
    ): CharSequence {
        val stream = `in`
        val reader: Reader = InputStreamReader(stream, charset)
        val out = StringBuilder(stream.available())
        val buf = CharArray(BUFFER_SIZE)
        var count: Int
        while (reader.read(buf).also { count = it } >= 0) {
            out.append(buf, 0, count)
        }
        return out
    }

    /**
     * Convert the stream bytes to a string.
     *
     * @param in the input.
     * @return the string.
     * @throws IOException if an I/O error occurs.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun toString(`in`: InputStream): String {
        return toCharSequence(`in`).toString()
    }

    /**
     * Convert the stream bytes to a string.
     *
     * @param in      the input.
     * @param charset the character encoding.
     * @return the string.
     * @throws IOException if an I/O error occurs.
     */
    @JvmStatic
    @Throws(IOException::class)
    fun toString(`in`: InputStream, charset: Charset = StandardCharsets.UTF_8): String {
        return toCharSequence(`in`, charset).toString()
    }
}

@Throws(IOException::class)
fun InputStream.readFully(): InputStream = StreamUtils.readFully(this)

@Throws(IOException::class)
fun InputStream.toString(charset: Charset = StandardCharsets.UTF_8): String = StreamUtils.toString(this, charset)