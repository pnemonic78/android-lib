package com.github.lang

val Boolean?.isTrue: Boolean
    get() = (this != null) && (this == true)

val Boolean?.isFalse: Boolean
    get() = (this != null) && (this == false)

fun CharSequence?.orEmpty(): CharSequence = this ?: ""

fun IntArray.min(fromIndex: Int, toIndex: Int): Int {
    if (isEmpty()) throw NoSuchElementException()
    var min = Int.MAX_VALUE
    for (i in fromIndex until toIndex) {
        val e = this[i]
        if (e < min) min = e
    }
    return min
}

fun IntArray.min(range: IntRange) = min(range.first, range.endInclusive + 1)

fun IntArray.max(fromIndex: Int, toIndex: Int): Int {
    if (isEmpty()) throw NoSuchElementException()
    var max = Int.MIN_VALUE
    for (i in fromIndex until toIndex) {
        val e = this[i]
        if (e > max) max = e
    }
    return max
}

fun IntArray.max(range: IntRange) = max(range.first, range.endInclusive + 1)
