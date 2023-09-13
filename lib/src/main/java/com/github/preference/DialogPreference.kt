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
package com.github.preference

import android.content.Context
import android.util.AttributeSet
import androidx.preference.DialogPreference
import com.github.lib.R

/**
 * A base class for [Preference] objects that are
 * dialog-based. These preferences will, when clicked, open a dialog showing the
 * actual preference controls.
 *
 * @author Moshe Waisberg
 */
open class DialogPreference @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = androidx.preference.R.attr.dialogPreferenceStyle,
    defStyleRes: Int = 0
) : DialogPreference(context, attrs, defStyleAttr, defStyleRes) {

    /**
     * The text of the neutral button of the dialog. This will be shown on subsequent dialogs.
     */
    var neutralButtonText: CharSequence? = null

    /**
     * Sets the text of the neutral button of the dialog. This will be shown on subsequent dialogs.
     *
     * @param neutralButtonTextResId The neutral button text as a resource.
     * @see .setNeutralButtonText
     */
    fun setNeutralButtonText(neutralButtonTextResId: Int) {
        neutralButtonText = context.getString(neutralButtonTextResId)
    }

    /**
     * The neutral button was clicked, so reset the value.
     */
    open fun onNeutralButtonClicked() = Unit
}