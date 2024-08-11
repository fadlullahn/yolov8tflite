package com.example.yolov8tflite

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat

class OverlayView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private var results = listOf<BoundingBox>()
    private var boxPaint = Paint()
    private var textBackgroundPaint = Paint()
    private var textPaint = Paint()

    private var bounds = Rect()

    init {
        initPaints()
    }

    fun clear() {
        results = listOf()
        textPaint.reset()
        textBackgroundPaint.reset()
        boxPaint.reset()
        invalidate()
        initPaints()
    }

    private fun initPaints() {
        // Text Paint
        textPaint.color = Color.WHITE // Warna teks putih
        textPaint.style = Paint.Style.FILL
        textPaint.textSize = 50f
        textPaint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD) // Font tebal dan stylish
        textPaint.setShadowLayer(4f, 2f, 2f, Color.BLACK) // Bayangan untuk teks

        // Box Paint
        boxPaint.color = ContextCompat.getColor(context!!, R.color.bounding_box_color) // Warna kotak pembatas hijau
        boxPaint.strokeWidth = 8F
        boxPaint.style = Paint.Style.STROKE
        boxPaint.pathEffect = DashPathEffect(floatArrayOf(50f, 10f), 0f) // Garis putus-putus
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        results.forEach {
            val left = it.x1 * width
            val top = it.y1 * height
            val right = it.x2 * width
            val bottom = it.y2 * height

            // Gambar kotak pembatas terlebih dahulu
            canvas.drawRect(left, top, right, bottom, boxPaint)

            // Siapkan teks dengan confidence
            val drawableText = "${it.clsName} (${String.format("%.2f", it.cnf)})"

            // Hitung ukuran teks
            textPaint.getTextBounds(drawableText, 0, drawableText.length, bounds)
            val textWidth = bounds.width()
            val textHeight = bounds.height()

            // Gambar teks di atas kotak pembatas
            val textX = left
            val textY = top - 10f // Sesuaikan offset vertikal agar teks berada di atas kotak

            canvas.drawText(drawableText, textX, textY, textPaint)
        }
    }



    fun setResults(boundingBoxes: List<BoundingBox>) {
        results = boundingBoxes
        invalidate()
    }

    companion object {
        private const val BOUNDING_RECT_TEXT_PADDING = 8
    }
}