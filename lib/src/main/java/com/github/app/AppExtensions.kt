package com.github.app

import android.app.PendingIntent
import android.os.Build
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.M)
@JvmField
val PendingIntent_FLAG_IMMUTABLE =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE else 0
