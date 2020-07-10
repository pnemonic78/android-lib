package com.github.storage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.storage.StorageManager
import android.os.storage.StorageVolume

class StorageManagerCompat {
    companion object {
        @SuppressLint("NewApi")
        fun getStorageVolumes(context: Context): List<StorageVolumeCompat> {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val volumes = storageManager.storageVolumes
                return volumes.map { StorageVolumeCompat.of(context, it) }
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val clazz = storageManager.javaClass
                val method = clazz.getMethod("getVolumeList")
                val volumes = method.invoke(storageManager) as Array<*>
                return volumes.map { StorageVolumeCompat.of(context, it as StorageVolume) }
            }
            return emptyList()
        }

        fun getExternalVolume(context: Context): StorageVolumeCompat? {
            val volumes = getStorageVolumes(context)
            var v: StorageVolumeCompat? = null

            for (volume in volumes) {
                if (volume.isRemovable) {
                    if ((v == null) || v.isEmulated) {
                        v = volume
                    } else if (volume.isPrimary) {
                        v = volume
                    }
                }
            }
            return v
        }
    }
}