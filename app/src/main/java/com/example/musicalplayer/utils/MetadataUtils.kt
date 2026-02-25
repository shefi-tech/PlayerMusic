package com.example.musicalplayer.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaMetadataRetriever

import com.example.musicalplayer.data.model.AudioTrack

object MetadataUtils {

    fun extractMetadata(context: Context, assetPath: String): AudioTrack {

        val retriever = MediaMetadataRetriever()
        val afd = context.assets.openFd(assetPath)

        retriever.setDataSource(
            afd.fileDescriptor,
            afd.startOffset,
            afd.length
        )

        val title = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_TITLE
        ) ?: "Unknown Title"

        val artist = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_ARTIST
        ) ?: "Unknown Artist"

        val duration = retriever.extractMetadata(
            MediaMetadataRetriever.METADATA_KEY_DURATION
        )?.toIntOrNull() ?: 0

        val artBytes = retriever.embeddedPicture
        val artwork: Bitmap? =
            artBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }

        retriever.release()
        afd.close()

        return AudioTrack(
            assetPath = assetPath,
            title = title,
            artist = artist,
            duration = duration,
            artwork = artwork
        )
    }

    fun getAlbumArt(context: Context, fileName: String): ByteArray? {
        val retriever = MediaMetadataRetriever()
        return try {
            val afd = context.assets.openFd(fileName)
            retriever.setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            retriever.embeddedPicture
        } catch (e: Exception) {
            null
        } finally {
            retriever.release()
        }
    }

}

