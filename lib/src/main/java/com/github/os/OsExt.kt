package com.github.os

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.contract

@OptIn(ExperimentalContracts::class)
fun Bundle?.isNullOrEmpty(): Boolean {
    contract {
        returns(false) implies (this@isNullOrEmpty != null)
    }

    return this == null || this.isEmpty
}

@Suppress("DEPRECATION")
fun <T : Parcelable> Bundle.getParcelableCompat(key: String, clazz: Class<T>): T? {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        return getParcelable(key, clazz)
    }
    return getParcelable(key)
}

fun <T : Parcelable> Intent.getParcelableCompat(key: String, clazz: Class<T>): T? {
    return extras?.getParcelableCompat(key, clazz)
}
