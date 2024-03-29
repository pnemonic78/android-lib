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
package com.github.text.style

import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Parcel
import android.text.TextPaint
import androidx.annotation.RequiresApi

/**
 * Changes the typeface family of the text to which the span is attached.
 *
 * This span does not work with [android.widget.RemoteViews] because [Typeface] is not [android.os.Parcelable].
 */
class TypefaceSpan : android.text.style.TypefaceSpan {

    private var typeface: Typeface? = null

    /**
     * Create a new span.
     *
     * @param family the font family for this typeface. Examples include "monospace", "serif", and
     * "sans-serif".
     */
    constructor(family: String) : super(family) {
        typeface = Typeface.create(family, Typeface.NORMAL)
    }

    /**
     * Create a new span.
     *
     * @param tf the typeface.
     */
    constructor(tf: Typeface) : super( "sans-serif") {
        typeface = tf
    }

    /**
     * Create a new span.
     *
     * @param src the parcel with the font family to read.
     */
    constructor(src: Parcel) : super(src)

    /**
     * Get the typeface.
     *
     * @return the typeface.
     */
    override fun getTypeface(): Typeface? {
        return typeface ?: super.getTypeface()
    }

    override fun updateDrawState(ds: TextPaint) {
        apply(ds, typeface)
    }

    override fun updateMeasureState(paint: TextPaint) {
        apply(paint, typeface)
    }

    private fun apply(paint: Paint, tf: Typeface?) {
        val oldStyle = paint.typeface?.style ?: Typeface.NORMAL

        val typeface = Typeface.create(tf, oldStyle)
        val fake = oldStyle and typeface.style.inv()
        if (fake and Typeface.BOLD != 0) {
            paint.isFakeBoldText = true
        }
        if (fake and Typeface.ITALIC != 0) {
            paint.textSkewX = -0.25f
        }
        paint.typeface = typeface
    }
}