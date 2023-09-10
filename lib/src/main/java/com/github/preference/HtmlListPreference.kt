/*
 * Copyright 2016, Moshe Waisberg
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
package com.github.preference

import android.content.Context
import android.text.Editable
import android.text.Html
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.util.AttributeSet
import androidx.preference.ListPreference

/**
 * List preference that re-parses the entries as HTML.
 *
 * @author Moshe Waisberg
 */
open class HtmlListPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : ListPreference(context, attrs) {

    init {
        this.entries = entries ?: emptyArray()
    }

    override fun setEntries(entries: Array<CharSequence?>?) {
        if (entries.isNullOrEmpty()) return
        val length = entries.size
        for (i in 0 until length) {
            val entry = entries[i] ?: continue
            entries[i] = trim(Html.fromHtml(entry.toString()))
        }
        super.setEntries(entries)
    }

    private fun trim(spanned: Spanned): Spanned {
        if (spanned.isEmpty()) {
            return spanned
        }
        val e: Editable = if (spanned is Editable) {
            spanned
        } else {
            SpannableStringBuilder.valueOf(spanned)
        }
        var length = e.length
        while (length > 0 && Character.isWhitespace(e[0])) {
            e.delete(0, 1)
            length--
        }
        var length1 = length - 1
        while (length > 0 && Character.isWhitespace(e[length1])) {
            e.delete(length1, length)
            length--
            length1--
        }
        return e
    }
}