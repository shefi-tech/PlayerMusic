package com.example.musicalplayer.ui.equalizer

import android.content.Context
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import com.example.musicalplayer.utils.EqualizerStorage

class EqualizerViewModel(private val context: Context) : ViewModel() {
    private val _bands = MutableStateFlow(
        EqualizerStorage.loadBands(context)
    )
    val bands: StateFlow<ShortArray> = _bands

    fun updateBands(newBands: ShortArray) {
        _bands.value = newBands
        EqualizerStorage.saveBands(context, newBands)
    }

    fun applyPreset(preset: String) {
        val presetBands = when (preset) {
            "Flat" -> shortArrayOf(0, 0, 0, 0, 0)
            "Rock" -> shortArrayOf(300, 200, 0, 200, 300)
            "Jazz" -> shortArrayOf(200, 100, 0, 100, 200)
            "Classical" -> shortArrayOf(0, 0, 200, 0, 0)
            "Pop" -> shortArrayOf(100, 200, 0, 200, 100)
            "Vocal" -> shortArrayOf(-100, 0, 200, 0, -100)
            else -> shortArrayOf(0, 0, 0, 0, 0)
        }

        _bands.value = presetBands
        EqualizerStorage.saveBands(context, presetBands)
    }


}

