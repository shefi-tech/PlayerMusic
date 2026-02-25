package com.example.musicalplayer.data.model

import android.graphics.Bitmap

data class AudioTrack(
    val assetPath: String,
    val title: String,
    val artist: String,
    val duration: Int,
    val artwork: Bitmap?
)