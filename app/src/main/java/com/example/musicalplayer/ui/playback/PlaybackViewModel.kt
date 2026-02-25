package com.example.musicalplayer.ui.playback

import androidx.lifecycle.ViewModel
import com.example.musicalplayer.data.model.AudioTrack
import com.example.musicalplayer.media.MediaPlayerManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PlaybackViewModel(
    private val mediaManager: MediaPlayerManager
) : ViewModel() {

    private val _currentTrack = MutableStateFlow<AudioTrack?>(null)


    val progress = mediaManager.progress

    fun loadTrack(track: AudioTrack) {
        _currentTrack.value = track
        mediaManager.load(track)
    }

    fun playPause() {
        if (mediaManager.isPlaying())
            mediaManager.pause()
        else
            mediaManager.play()
    }

    fun seekTo(position: Int) {
        mediaManager.seekTo(position)
    }

    override fun onCleared() {
        mediaManager.release()
    }
}
