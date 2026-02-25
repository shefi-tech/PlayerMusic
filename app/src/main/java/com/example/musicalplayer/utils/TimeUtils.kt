package com.example.musicalplayer.utils

object TimeUtils {

    fun format(milliseconds: Int): String {

        val totalSeconds = milliseconds / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60

        return String.format("%02d:%02d", minutes, seconds)
    }
}