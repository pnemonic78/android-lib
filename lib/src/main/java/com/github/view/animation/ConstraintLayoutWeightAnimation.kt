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
package com.github.view.animation

import android.view.View
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.constraintlayout.widget.ConstraintLayout

class ConstraintLayoutWeightAnimation(
    private val view: View,
    private val fromWeight: Float,
    private val toWeight: Float
) : Animation() {

    private val increase: Boolean = toWeight >= fromWeight

    override fun willChangeBounds(): Boolean = true

    override fun willChangeTransformationMatrix(): Boolean = false

    override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
        val lp = view.layoutParams as ConstraintLayout.LayoutParams
        if (increase) {
            lp.horizontalWeight = (toWeight - fromWeight) * interpolatedTime
        } else {
            lp.horizontalWeight = (fromWeight - toWeight) * (1f - interpolatedTime)
        }
        view.layoutParams = lp
    }
}