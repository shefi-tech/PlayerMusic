package com.example.musicalplayer.ui.playback

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.musicalplayer.R
import com.example.musicalplayer.databinding.ActivityPlaybackBinding
import com.example.musicalplayer.media.MediaPlayerManager
import com.example.musicalplayer.ui.equalizer.EqualizerActivity
import com.example.musicalplayer.utils.EqualizerStorage
import com.example.musicalplayer.utils.MetadataUtils
import com.example.musicalplayer.utils.TimeUtils
import com.example.musicalplayer.utils.WaveformView
import kotlinx.coroutines.launch

class PlaybackActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaybackBinding
    private lateinit var mediaManager: MediaPlayerManager
    private lateinit var viewModel: PlaybackViewModel
    private lateinit var  waveformView: WaveformView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlaybackBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mediaManager = MediaPlayerManager(this)

        viewModel = PlaybackViewModel(mediaManager)

        val track = MetadataUtils.extractMetadata(this, "sample.mp3")

        viewModel.loadTrack(track)

        binding.seekBar.max = track.duration

        observeProgress()

        binding.playPauseBtn.setOnClickListener {
            viewModel.playPause()
            updatePlayButton()
        }

        binding.equaliserBtn.setOnClickListener {
            val intent = Intent(this, EqualizerActivity::class.java)
            startActivity(intent)
        }

        binding.seekBar.setOnSeekBarChangeListener(
            object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    if (fromUser) {
                        viewModel.seekTo(progress)
                    }
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            }
        )

        val artBytes = MetadataUtils.getAlbumArt(this, "sample.mp3")

        if (artBytes != null) {
            val bitmap = BitmapFactory.decodeByteArray(artBytes, 0, artBytes.size)
            binding.albumArt.setImageBitmap(bitmap)
        } else {
            binding.albumArt.setImageResource(R.drawable.default_album)
        }

        viewModel.loadTrack(track)

        binding.titleText.text = track.title
        binding.seekBar.max = track.duration
        binding.artistText.text = track.artist

        waveformView = findViewById(R.id.waveformView)
        waveformView.setWaveform(generateWaveform())

    }

    private fun observeProgress() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.progress.collect { position ->
                    binding.seekBar.progress = position
                    binding.timeText.text = TimeUtils.format(position)
                }
            }
        }
    }

    private fun updatePlayButton() {
        if (mediaManager.isPlaying()) {
            binding.playPauseBtn.text = "Pause"
        } else {
            binding.playPauseBtn.text = "Play"
        }
    }

    override fun onPause() {
        super.onPause()
        if (mediaManager.isPlaying()) {
            mediaManager.pause()
            updatePlayButton()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaManager.release()
    }
    override fun onResume() {
        super.onResume()

        val savedBands = EqualizerStorage.loadBands(this)
        mediaManager.applyBands(savedBands)

        val bass = EqualizerStorage.loadBass(this)
        val treble = EqualizerStorage.loadTreble(this)

        mediaManager.setBassBoost(bass)
        mediaManager.setTrebleBoost(treble)
    }

    private fun generateWaveform(): List<Float> {
        val random = java.util.Random()
        return List(100) {
            random.nextFloat() * 0.8f + 0.2f
        }
    }
}
