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
package com.github.text.method

import android.text.Spanned
import android.text.method.DigitsKeyListener
import kotlin.math.max
import kotlin.math.round

/**
 * Number range input filter.
 *
 * @author Moshe Waisberg
 */
open class RangeInputFilter(
    protected val sign: Boolean,
    protected val decimal: Boolean,
    minimum: Double,
    maximum: Double
) : DigitsKeyListener(sign, decimal) {

    protected val minimum: Double
    protected val maximum: Double

    /**
     * Creates a new range filter with only non-negative integer values.
     *
     * @param minimum the minimum value.
     * @param maximum the maximum value.
     */
    constructor(minimum: Int, maximum: Int) : this(false, minimum, maximum)

    /**
     * Creates a new range filter with only integer values.
     *
     * @param sign    accepts the minus sign (only at the beginning)?
     * @param minimum the minimum value.
     * @param maximum the maximum value.
     */
    constructor(sign: Boolean, minimum: Int, maximum: Int) : this(
        sign,
        false,
        minimum.toDouble(),
        maximum.toDouble()
    )

    init {
        val mini = if (decimal) minimum else round(minimum)
        val maxi = if (decimal) maximum else round(maximum)
        this.minimum = if (sign) mini else max(0.0, mini)
        this.maximum = if (sign) maxi else max(0.0, maxi)
    }

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned,
        dstart: Int,
        dend: Int
    ): CharSequence {
        var out = super.filter(source, start, end, dest, dstart, dend)
        out = out ?: source.subSequence(start, end)
        var s = dest.toString()
        s = s.substring(0, dstart) + out + s.substring(dend)
        if (!s.isEmpty()) {
            if (sign) {
                if ("-" == s || "+" == s) {
                    return out
                }
                if (decimal && ("." == s || "-." == s || "+." == s)) {
                    return out
                }
            } else if (decimal && "." == s) {
                return out
            }
            val value = s.toDouble()
            if (value < minimum || value > maximum) {
                return ""
            }
        }
        return out
    }
}