package com.github.media

import android.content.Context
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.MediaMetadataRetriever
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import androidx.core.content.ContextCompat.createAttributionContext
import com.github.media.RingtoneManager.Companion.resolveUri
import timber.log.Timber

class RingtoneCompat(context: Context, attributionTag: String? = null) {
    private val context: Context = createAttributionContext(context, attributionTag)

    private val player: MediaPlayer =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            MediaPlayer(this.context)
        } else {
            MediaPlayer()
        }
    private var metadataRetriever: MediaMetadataRetriever? = null

    var isLooping: Boolean
        get() = player.isLooping
        set(value) {
            player.isLooping = value
        }
    val isPlaying: Boolean get() = player.isPlaying

    fun setAudioAttributes(audioAttributes: AudioAttributes) {
        player.setAudioAttributes(audioAttributes)
    }

    fun setDataSource(context: Context, uri: Uri) {
        val dataUri = resolveUri(context, uri) ?: Uri.EMPTY
        player.setDataSource(context, dataUri)
        metadataRetriever = MediaMetadataRetriever().apply {
            setDataSource(context, dataUri)
        }
    }

    fun prepare() {
        player.prepare()
    }

    fun play() {
        player.start()
    }

    fun stop() {
        player.stop()
    }

    fun getTitle(): String? {
        val retriever = metadataRetriever ?: return null
        return try {
            retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }

    fun setStreamType(streamType: Int) {
        val usage = when (streamType) {
            AudioManager.STREAM_ALARM -> AudioAttributes.USAGE_ALARM
            AudioManager.STREAM_NOTIFICATION -> AudioAttributes.USAGE_NOTIFICATION
            else -> AudioAttributes.USAGE_MEDIA
        }

        val audioAttributes = AudioAttributes.Builder()
            .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
            .setLegacyStreamType(streamType)
            .setUsage(usage)
            .build()

        setAudioAttributes(audioAttributes)
    }

    fun setType(ringtoneType: Int) {
        val streamType = when (ringtoneType) {
            RingtoneManager.TYPE_ALARM -> AudioManager.STREAM_ALARM
            RingtoneManager.TYPE_NOTIFICATION -> AudioManager.STREAM_NOTIFICATION
            else -> AudioManager.STREAM_RING
        }
        setStreamType(streamType)
    }
}