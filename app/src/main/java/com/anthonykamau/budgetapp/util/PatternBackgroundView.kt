package com.anthonykamau.budgetapp.util

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class PatternBackgroundView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    var patternType: String = "dots"

    private val dotRadius = 2.5f
    private val dotSpacing = 28f

    private val dotPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = 0xFFF59E0B.toInt()
        alpha = 15
        style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        when (patternType) {
            "dots" -> drawDots(canvas)
            "lines" -> drawLines(canvas)
            "circles" -> drawCircles(canvas)
            else -> drawDots(canvas)
        }
    }

    private fun drawDots(canvas: Canvas) {
        val w = width.toFloat()
        val h = height.toFloat()
        var x = dotSpacing
        while (x < w) {
            var y = dotSpacing
            while (y < h) {
                canvas.drawCircle(x, y, dotRadius, dotPaint)
                y += dotSpacing
            }
            x += dotSpacing
        }
    }

    private fun drawLines(canvas: Canvas) {
        val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFF59E0B.toInt()
            alpha = 10
            style = Paint.Style.STROKE
            strokeWidth = 1f
        }
        var y = 0f
        while (y < height) {
            canvas.drawLine(0f, y, width.toFloat(), y + 80f, linePaint)
            y += 40f
        }
    }

    private fun drawCircles(canvas: Canvas) {
        val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFFF59E0B.toInt()
            alpha = 8
            style = Paint.Style.STROKE
            strokeWidth = 1.5f
        }
        val cx = width / 2f
        val cy = height / 2f
        var radius = 60f
        while (radius < width) {
            canvas.drawCircle(cx, cy, radius, circlePaint)
            radius += 60f
        }
    }
}