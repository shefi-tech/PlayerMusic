package com.example.musicalplayer.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class WaveformView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var waveformData: List<Float> = emptyList()
    private var progress: Float = 0f

    private val playedPaint = Paint().apply {
        color = Color.GREEN
        strokeWidth = 6f
    }

    private val unplayedPaint = Paint().apply {
        color = Color.GRAY
        strokeWidth = 6f
    }

    fun setWaveform(data: List<Float>) {
        waveformData = data
        invalidate()
    }

    fun setProgress(value: Float) {
        progress = value
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (waveformData.isEmpty()) return

        val widthPerBar = width.toFloat() / waveformData.size
        val centerY = height / 2f

        waveformData.forEachIndexed { index, amplitude ->
            val x = index * widthPerBar
            val barHeight = amplitude * height

            val paint = if (index / waveformData.size.toFloat() <= progress) {
                playedPaint
            } else {
                unplayedPaint
            }

            canvas.drawLine(
                x,
                centerY - barHeight / 2,
                x,
                centerY + barHeight / 2,
                paint
            )
        }
    }
}