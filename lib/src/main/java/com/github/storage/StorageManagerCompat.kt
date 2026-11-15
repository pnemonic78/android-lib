package com.github.storage

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.os.storage.StorageManager

class StorageManagerCompat {
    companion object {
        @SuppressLint("NewApi")
        fun getStorageVolumes(context: Context): List<StorageVolumeCompat> {
            val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
            val volumes = storageManager.storageVolumes
            return volumes.map { StorageVolumeCompat.of(context, it) }
        }

        fun getExternalVolume(
            context: Context,
            isMounted: Boolean = true,
            isRemovable: Boolean = false
        ): StorageVolumeCompat? {
            val volumes = getStorageVolumes(context)
            var v: StorageVolumeCompat? = null

            for (volume in volumes) {
                if (isMounted && (volume.state != Environment.MEDIA_MOUNTED)) {
                    continue
                }
                if (isRemovable && volume.isRemovable) {
                    if (volume.isPrimary) {
                        return volume
                    }
                } else if (volume.isPrimary) {
                    return volume
                }
                if ((v == null) || v.isEmulated) {
                    v = volume
                }
            }
            return v
        }

        /**
         * Return the primary shared/external storage volume available to the current user. This volume is the same storage device returned by `Environment#getExternalStorageDirectory()` and `Context#getExternalFilesDir(String)`.
         */
        fun getPrimaryStorageVolume(context: Context): StorageVolumeCompat {
            val volumes = getStorageVolumes(context)
            return volumes.first { it.isPrimary }
        }
    }
}