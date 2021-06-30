package com.github.storage

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.os.Environment
import java.io.File

/**
 * Information about a shared/external storage volume for a specific user.
 * @see `android.os.storage.StorageVolume`
 */
class StorageVolumeCompat private constructor(
    val id: String,
    val directory: File,
    val description: String,
    val isPrimary: Boolean,
    val isRemovable: Boolean,
    val isEmulated: Boolean,
    val allowMassStorage: Boolean,
    val maxFileSize: Long,
    val uuid: String?,
    val state: String
) {

    fun getDescription(context: Context): String {
        return description
    }

    fun getMediaStoreVolumeName(): String? {
        return id
    }

    override fun hashCode(): Int {
        return directory.hashCode()
    }

    override fun toString(): String {
        val buffer: StringBuilder = StringBuilder("StorageVolume: ").append(description)
        if (uuid != null) {
            buffer.append(" (").append(uuid).append(")")
        }
        return buffer.toString()
    }

    companion object {
        fun of(
            context: Context,
            storageVolume: android.os.storage.StorageVolume
        ): StorageVolumeCompat {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                return toExternalVolume30(context, storageVolume)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                return toExternalVolume24(context, storageVolume)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                return toExternalVolume21(context, storageVolume)
            }
            return toExternalVolume19(context, storageVolume)
        }

        @TargetApi(Build.VERSION_CODES.R)
        private fun toExternalVolume30(
            context: Context,
            storageVolume: android.os.storage.StorageVolume
        ): StorageVolumeCompat {
            val id = getString(storageVolume, "getId")!!
            val directory = storageVolume.directory ?: getFile(storageVolume, "getDirectory")
            val description =
                getString(storageVolume, "getDescription", Context::class.java, context) ?: ""
            val isPrimary = storageVolume.isPrimary
            val isRemovable = storageVolume.isRemovable
            val isEmulated = storageVolume.isEmulated
            val allowMassStorage = getBoolean(storageVolume, "allowMassStorage")
            val maxFileSize = getLong(storageVolume, "getMaxFileSize")
            val uuid = storageVolume.uuid
            val state = storageVolume.state
            return StorageVolumeCompat(
                id,
                directory,
                description,
                isPrimary,
                isRemovable,
                isEmulated,
                allowMassStorage,
                maxFileSize,
                uuid,
                state
            )
        }

        @TargetApi(Build.VERSION_CODES.N)
        private fun toExternalVolume24(
            context: Context,
            storageVolume: android.os.storage.StorageVolume
        ): StorageVolumeCompat {
            val id = getString(storageVolume, "getId")!!
            val directory = getFile(storageVolume, "getPathFile")
            val description =
                getString(storageVolume, "getDescription", Context::class.java, context) ?: ""
            val isPrimary = storageVolume.isPrimary
            val isRemovable = storageVolume.isRemovable
            val isEmulated = storageVolume.isEmulated
            val allowMassStorage = getBoolean(storageVolume, "allowMassStorage")
            val maxFileSize = getLong(storageVolume, "getMaxFileSize")
            val uuid = storageVolume.uuid
            val state = storageVolume.state
            return StorageVolumeCompat(
                id,
                directory,
                description,
                isPrimary,
                isRemovable,
                isEmulated,
                allowMassStorage,
                maxFileSize,
                uuid,
                state
            )
        }

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        private fun toExternalVolume21(
            context: Context,
            storageVolume: android.os.storage.StorageVolume
        ): StorageVolumeCompat {
            val id = getInt(storageVolume, "getStorageId").toString()
            val directory = getFile(storageVolume, "getPathFile")
            val description =
                getString(storageVolume, "getDescription", Context::class.java, context) ?: ""
            val isPrimary = getBoolean(storageVolume, "isPrimary")
            val isRemovable = getBoolean(storageVolume, "isRemovable")
            val isEmulated = getBoolean(storageVolume, "isEmulated")
            val allowMassStorage = getBoolean(storageVolume, "allowMassStorage")
            val maxFileSize = getLong(storageVolume, "getMaxFileSize")
            val uuid = getString(storageVolume, "getUuid")
            val state = getString(storageVolume, "getState")!!
            return StorageVolumeCompat(
                id,
                directory,
                description,
                isPrimary,
                isRemovable,
                isEmulated,
                allowMassStorage,
                maxFileSize,
                uuid,
                state
            )
        }

        @TargetApi(Build.VERSION_CODES.KITKAT)
        private fun toExternalVolume19(
            context: Context,
            storageVolume: android.os.storage.StorageVolume
        ): StorageVolumeCompat {
            val id = getString(storageVolume, "getStorageId")!!
            val directory = getFile(storageVolume, "getPathFile")
            val description =
                getString(storageVolume, "getDescription", Context::class.java, context) ?: ""
            val isPrimary = getBoolean(storageVolume, "isPrimary")
            val isRemovable = getBoolean(storageVolume, "isRemovable")
            val isEmulated = getBoolean(storageVolume, "isEmulated")
            val allowMassStorage = getBoolean(storageVolume, "allowMassStorage")
            val maxFileSize = getLong(storageVolume, "getMaxFileSize")
            val uuid = null
            val state = Environment.MEDIA_UNKNOWN
            return StorageVolumeCompat(
                id,
                directory,
                description,
                isPrimary,
                isRemovable,
                isEmulated,
                allowMassStorage,
                maxFileSize,
                uuid,
                state
            )
        }

        private fun getBoolean(target: Any, methodName: String): Boolean {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName)
            return method.invoke(target) as Boolean
        }

        private fun getFile(target: Any, methodName: String): File {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName)
            return method.invoke(target) as File
        }

        private fun getInt(target: Any, methodName: String): Int {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName)
            return method.invoke(target) as Int
        }

        private fun getLong(target: Any, methodName: String): Long {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName)
            return method.invoke(target) as Long
        }

        private fun getString(target: Any, methodName: String): String? {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName)
            return method.invoke(target) as String
        }

        private fun getString(
            target: Any,
            methodName: String,
            clazz0: Class<*>,
            arg0: Any
        ): String? {
            val clazz = target.javaClass
            val method = clazz.getMethod(methodName, clazz0)
            return method.invoke(target, arg0) as String
        }
    }
}