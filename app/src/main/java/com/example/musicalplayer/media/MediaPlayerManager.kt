package com.example.musicalplayer.media

import android.content.Context
import android.media.MediaPlayer
import android.media.audiofx.BassBoost
import android.media.audiofx.Equalizer
import com.example.musicalplayer.data.model.AudioTrack
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MediaPlayerManager(private val context: Context) {

    private var mediaPlayer: MediaPlayer? = null
    private var equalizer: Equalizer? = null

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _progress = MutableStateFlow(0)
    val progress: StateFlow<Int> = _progress
    private var bassBoost: BassBoost? = null

    fun load(track: AudioTrack) {
        releasePlayerOnly()

        val afd = context.assets.openFd(track.assetPath)

        mediaPlayer = MediaPlayer().apply {
            setDataSource(afd.fileDescriptor, afd.startOffset, afd.length)
            prepare()
        }

        attachEqualizer()
        startProgressUpdates()
    }

    fun play() {
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun seekTo(position: Int) {
        mediaPlayer?.seekTo(position)
    }


    fun isPlaying(): Boolean = mediaPlayer?.isPlaying ?: false

    private fun startProgressUpdates() {
        scope.launch {
            while (isActive) {
                delay(500)
                _progress.value = mediaPlayer?.currentPosition ?: 0
            }
        }
    }

    fun attachEqualizer() {
        val sessionId = mediaPlayer?.audioSessionId ?: return

        if (equalizer != null) return

        equalizer = Equalizer(0, sessionId).apply {
            enabled = true
        }
    }

    fun applyBands(bands: ShortArray) {
        equalizer?.let { eq ->
            val numberOfBands = eq.numberOfBands

            for (i in bands.indices) {
                if (i < numberOfBands) {
                    eq.setBandLevel(i.toShort(), bands[i])
                }
            }
        }
    }

    private fun releasePlayerOnly() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun release() {
        scope.cancel()

        equalizer?.release()
        equalizer = null

        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun setBassBoost(strength: Int) {
        if (bassBoost == null) {
            bassBoost = mediaPlayer?.let { BassBoost(0, it.audioSessionId) }
            bassBoost?.enabled = true
        }
        bassBoost?.setStrength(strength.toShort())
    }

    fun setTrebleBoost(strength: Int) {

        equalizer?.let {
            val highestBand = it.numberOfBands - 1
            it.setBandLevel(highestBand.toShort(), strength.toShort())
        }
    }
}