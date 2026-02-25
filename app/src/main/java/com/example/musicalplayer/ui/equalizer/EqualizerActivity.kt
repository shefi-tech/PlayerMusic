package com.example.musicalplayer.ui.equalizer

import android.os.Bundle
import android.widget.Button
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.musicalplayer.R
import com.example.musicalplayer.media.MediaPlayerManager
import com.example.musicalplayer.utils.CircularKnobView
import kotlinx.coroutines.launch
import com.example.musicalplayer.utils.EqualizerStorage


class EqualizerActivity : AppCompatActivity() {

    private lateinit var viewModel: EqualizerViewModel
    private lateinit var mediaManager: MediaPlayerManager
    private lateinit var bassKnob: CircularKnobView
    private lateinit var trebleKnob: CircularKnobView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_equalizer)
        bassKnob = findViewById(R.id.bassKnob)
        trebleKnob = findViewById(R.id.trebleKnob)
       mediaManager = MediaPlayerManager(this)
        viewModel = EqualizerViewModel(applicationContext)

        mediaManager.attachEqualizer()

        lifecycleScope.launch {
            viewModel.bands.collect { bands ->
                mediaManager.applyBands(bands)
                EqualizerStorage.saveBands(this@EqualizerActivity, bands)
                updateUI(bands)
            }
        }

        setupPresetButtons()
        setupBandControls()

        mediaManager.attachEqualizer()

        lifecycleScope.launch {
            viewModel.bands.collect { bands ->
                mediaManager.applyBands(bands)
            }
        }

        setupPresetButtons()
        bassKnob.setOnProgressChangedListener { progress ->
            EqualizerStorage.saveBass(this, progress)
            mediaManager.setBassBoost(progress)
        }

        trebleKnob.setOnProgressChangedListener { progress ->
            EqualizerStorage.saveTreble(this, progress)
            mediaManager.setTrebleBoost(progress)
        }


    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
    private fun setupPresetButtons() {
        findViewById<Button>(R.id.btnRock).setOnClickListener {
            viewModel.applyPreset("Rock")
        }
        findViewById<Button>(R.id.btnJazz).setOnClickListener {
            viewModel.applyPreset("Jazz")
        }
        findViewById<Button>(R.id.btnFlat).setOnClickListener {
            viewModel.applyPreset("Flat")
        }
        findViewById<Button>(R.id.btnClassical).setOnClickListener {
            viewModel.applyPreset("Classical")
        }
        findViewById<Button>(R.id.btnPop).setOnClickListener {
            viewModel.applyPreset("Pop")
        }
        findViewById<Button>(R.id.btnVocal).setOnClickListener {
            viewModel.applyPreset("Vocal")
        }
    }

    private fun setupBandControls() {

        val bands = EqualizerStorage.loadBands(this)

        val bandSeekBars = listOf(
            findViewById<SeekBar>(R.id.band1),
            findViewById<SeekBar>(R.id.band2),
            findViewById<SeekBar>(R.id.band3),
            findViewById<SeekBar>(R.id.band4),
            findViewById<SeekBar>(R.id.band5)
        )

        bandSeekBars.forEachIndexed { index, seekBar ->
            seekBar.max = 1000
            seekBar.progress = bands[index] + 500

            seekBar.setOnSeekBarChangeListener(object :
                SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(sb: SeekBar?, p: Int, fromUser: Boolean) {
                    if (fromUser) {
                        val newBands = bands.clone()
                        newBands[index] = (p - 500).toShort()
                        viewModel.updateBands(newBands)
                    }
                }
                override fun onStartTrackingTouch(sb: SeekBar?) {}
                override fun onStopTrackingTouch(sb: SeekBar?) {}
            })
        }
    }

    override fun onResume() {
        super.onResume()

        mediaManager.attachEqualizer()

        val savedBands = EqualizerStorage.loadBands(this)
        mediaManager.applyBands(savedBands)
    }

    private fun updateUI(bands: ShortArray) {

        val bandSeekBars = listOf(
            findViewById<SeekBar>(R.id.band1),
            findViewById<SeekBar>(R.id.band2),
            findViewById<SeekBar>(R.id.band3),
            findViewById<SeekBar>(R.id.band4),
            findViewById<SeekBar>(R.id.band5)
        )

        bandSeekBars.forEachIndexed { index, seekBar ->
            if (index < bands.size) {
                // Offset because SeekBar cannot handle negative values
                seekBar.progress = bands[index] + 500
            }
        }
    }


}
