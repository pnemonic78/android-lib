package com.github.math

fun Double.toRadians(): Double = Math.toRadians(this)

fun Double.toDegrees(): Double = Math.toDegrees(this)

fun Float.toRadians(): Float = this.toDouble().toRadians().toFloat()

fun Float.toDegrees(): Float = this.toDouble().toDegrees().toFloat()

val Int.isEven get() = this.and(1) == 0

val Int.isOdd get() = this.and(1) == 1
