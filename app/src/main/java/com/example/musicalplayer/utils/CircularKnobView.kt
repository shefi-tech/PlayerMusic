package com.example.musicalplayer.utils

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.atan2
import kotlin.math.min


class CircularKnobView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : View(context, attrs) {

    private var angle = 0f
    private var maxAngle = 270f
    private var listener: ((Int) -> Unit)? = null

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.GRAY
    }

    private val indicatorPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 20f
        color = Color.BLUE
    }

    fun setOnProgressChangedListener(l: (Int) -> Unit) {
        listener = l
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val size = min(width, height)
        val radius = size / 2f - 30

        canvas.drawArc(
            30f,
            30f,
            width - 30f,
            height - 30f,
            135f,
            maxAngle,
            false,
            paint
        )

        canvas.drawArc(
            30f,
            30f,
            width - 30f,
            height - 30f,
            135f,
            angle,
            false,
            indicatorPaint
        )
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x - width / 2f
        val y = event.y - height / 2f
        val touchAngle = Math.toDegrees(atan2(y.toDouble(), x.toDouble())).toFloat() + 180f

        angle = ((touchAngle - 135f).coerceIn(0f, maxAngle))
        val progress = ((angle / maxAngle) * 100).toInt()

        listener?.invoke(progress)
        invalidate()
        return true
    }
}