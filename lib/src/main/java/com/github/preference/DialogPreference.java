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
package com.github.preference;

import android.content.Context;
import android.util.AttributeSet;

import androidx.preference.Preference;

/**
 * A base class for {@link Preference} objects that are
 * dialog-based. These preferences will, when clicked, open a dialog showing the
 * actual preference controls.
 *
 * @author Moshe Waisberg
 */
public class DialogPreference extends androidx.preference.DialogPreference {

    private CharSequence neutralButtonText;

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public DialogPreference(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public DialogPreference(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DialogPreference(Context context) {
        super(context);
    }

    /**
     * Sets the text of the neutral button of the dialog. This will be shown on subsequent dialogs.
     *
     * @param neutralButtonText The text of the neutral button.
     */
    public void setNeutralButtonText(CharSequence neutralButtonText) {
        this.neutralButtonText = neutralButtonText;
    }

    /**
     * Sets the text of the neutral button of the dialog. This will be shown on subsequent dialogs.
     *
     * @param neutralButtonTextResId The neutral button text as a resource.
     * @see #setNeutralButtonText(CharSequence)
     */
    public void setNeutralButtonText(int neutralButtonTextResId) {
        setNeutralButtonText(getContext().getString(neutralButtonTextResId));
    }

    /**
     * Returns the text of the neutral button to be shown on subsequent dialogs.
     *
     * @return The text of the neutral button.
     */
    public CharSequence getNeutralButtonText() {
        return neutralButtonText;
    }

    /**
     * The neutral button was clicked, so reset the value.
     */
    public void onNeutralButtonClicked() {
    }
}
