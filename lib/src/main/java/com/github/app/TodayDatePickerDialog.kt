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
package com.github.app

import android.app.DatePickerDialog
import android.content.Context
import android.content.DialogInterface
import com.github.lib.R
import com.github.util.dayOfMonth
import com.github.util.month
import com.github.util.year
import java.util.Calendar

/**
 * Date picker dialog with a "Today" button.
 *
 * @author Moshe Waisberg
 */
class TodayDatePickerDialog : DatePickerDialog {
    constructor(
        context: Context,
        callBack: OnDateSetListener?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) : super(context, callBack, year, monthOfYear, dayOfMonth) {
        init(context)
    }

    constructor(
        context: Context,
        theme: Int,
        callBack: OnDateSetListener?,
        year: Int,
        monthOfYear: Int,
        dayOfMonth: Int
    ) : super(context, theme, callBack, year, monthOfYear, dayOfMonth) {
        init(context)
    }

    private fun init(context: Context) {
        setButton(BUTTON_NEUTRAL, context.getText(R.string.today), this)
    }

    override fun onClick(dialog: DialogInterface, which: Int) {
        if (which == BUTTON_NEUTRAL) {
            setToday()
            onClick(dialog, BUTTON_POSITIVE)
        }
        super.onClick(dialog, which)
    }

    private fun setToday() {
        val today = Calendar.getInstance()
        updateDate(today.year, today.month, today.dayOfMonth)
    }
}