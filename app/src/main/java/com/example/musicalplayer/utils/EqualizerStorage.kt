package com.example.musicalplayer.utils

import android.content.Context

object EqualizerStorage {

    private const val PREF_NAME = "equalizer_prefs"
    private const val KEY_BANDS = "bands"

    private const val KEY_BASS = "bass"
    private const val KEY_TREBLE = "treble"

    fun saveBands(context: Context, bands: ShortArray) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val editor = prefs.edit()

        val bandString = bands.joinToString(",")
        editor.putString(KEY_BANDS, bandString)
        editor.apply()
    }

    fun loadBands(context: Context): ShortArray {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        val saved = prefs.getString(KEY_BANDS, null)

        return if (saved != null) {
            saved.split(",").map { it.toShort() }.toShortArray()
        } else {
            shortArrayOf(0, 0, 0, 0, 0) // default
        }
    }

    fun saveBass(context: Context, value: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_BASS, value)
            .apply()
    }

    fun saveTreble(context: Context, value: Int) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putInt(KEY_TREBLE, value)
            .apply()
    }

    fun loadBass(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_BASS, 0)
    }

    fun loadTreble(context: Context): Int {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getInt(KEY_TREBLE, 0)
    }
}