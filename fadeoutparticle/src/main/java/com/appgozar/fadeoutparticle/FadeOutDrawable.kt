package com.appgozar.fadeoutparticle

import android.graphics.*
import android.view.View
import kotlin.random.Random

internal class FadeOutDrawable(private val density: Float) {
    private val rectF = RectF()
    private var particles: ArrayList<Particle>? = null
    private val particlePaint = Paint()
    private val fadePaint = Paint().apply {
        shader =
            LinearGradient(
                0f, 0f, 10f.toPx(), 0f,
                Color.WHITE, Color.TRANSPARENT, Shader.TileMode.CLAMP,
            )
        xfermode = PorterDuffXfermode(PorterDuff.Mode.MULTIPLY)
    }
    private val fadeMatrix = Matrix()

    fun onLayout(left: Int, top: Int, right: Int, bottom: Int) {
        rectF.right = (right - left).toFloat()
        rectF.bottom = (bottom - top).toFloat()
    }

    fun draw(canvas: Canvas, animationValue: Float) {
        if (animationValue != 0f) {
            val width = rectF.width()
            val targetWidth = width * (1 - animationValue * 2f)
            fadeMatrix.reset()
            fadeMatrix.postTranslate(targetWidth, 0f)
            fadePaint.shader.setLocalMatrix(fadeMatrix)
            canvas.drawRect(rectF, fadePaint)
            particles?.run { drawParticles(canvas, width, targetWidth) }
        }
    }

    private fun drawParticles(canvas: Canvas, width: Float, limit: Float) {
        val particles = particles ?: return
        val xCoe = 8.toPx()
        val yCoe = 2.toPx()
        for (particle in particles) {
            if (particle.cx <= limit) {
                continue
            }
            val particleProgress = (particle.cx - limit).progress(width)
            particlePaint.color = particle.color
            particlePaint.alpha = (particlePaint.alpha * particleProgress.animateAlpha()).toInt()
            val px = (1 + PARTICLE_MAX_RADIUS - particle.radius) / PARTICLE_MAX_RADIUS
            val py = (particle.cx + particle.cy * 3) % 16 - 8
            canvas.drawCircle(
                particle.cx + particleProgress * xCoe * px,
                particle.cy + particleProgress.interpolateYAxis(particle.pathType) * yCoe * py,
                particle.radius,
                particlePaint,
            )

        }
    }

    fun prepare(child: View?) {
        if (child != null) {
            child.toBitmapOrNull(BITMAP_SCALE)?.also { bitmap ->
                generateParticles(bitmap)
                bitmap.recycle()
            }
        }
    }

    private fun generateParticles(bitmap: Bitmap) {
        val space = 3
        val spacePx = space.toPx()
        val verticalCount = Math.max(1, (rectF.height() / spacePx).toInt())
        val horizontalCount = Math.max(1, (rectF.width() / spacePx).toInt())
        val particles = ArrayList<Particle>(verticalCount * horizontalCount).also {
            particles = it
        }
        var horizontalOffset: Float
        var verticalOffset = spacePx / 2f
        for (i in 0 until verticalCount) {
            horizontalOffset = 0f
            for (j in 0 until horizontalCount) {
                val radius = Random.nextDouble(
                    PARTICLE_MIN_RADIUS.toDouble(),
                    PARTICLE_MAX_RADIUS.toDouble(),
                ).toFloat()
                val particle = Particle(
                    cx = horizontalOffset,
                    cy = verticalOffset,
                    radius = radius.toPx(),
                    color = bitmap.getPixel(
                        (horizontalOffset * BITMAP_SCALE).toInt(),
                        (verticalOffset * BITMAP_SCALE).toInt(),
                        space,
                    ).withCoefficientOpacity(radius / PARTICLE_MAX_RADIUS),
                    pathType = Random.nextInt(0, 5),
                )
                particles.add(particle)

                horizontalOffset += spacePx
            }
            verticalOffset += spacePx
        }
    }

    private fun Float.animateAlpha() = 1 - this

    private fun Bitmap.getPixel(x: Int, y: Int, space: Int): Int {
        val effectiveX = Math.max(0, x - space)
        val color = getPixel(effectiveX, y)
        if (effectiveX != 0 && space != 0 && color == Color.TRANSPARENT) {
            return getPixel(x, y, space - 1)
        }
        return color
    }

    private fun Float.interpolateYAxis(type: Int): Float {
        return when (type) {
            0 -> (this * this * ((1.7f + 1f) * this - 1.7f))
            1 -> -Math.pow(2.0, -10.0 * this).toFloat() + 1
            2 -> Math.sqrt(1.0 - (this - 1) * (this - 1)).toFloat()
            3 -> 1f - Math.cos(this * Math.PI / 2.0).toFloat()
            4 -> Math.pow(this.toDouble(), 3.0).toFloat()
            else -> Math.pow(2.0, 10.0 * (this - 1)).toFloat()
        }
    }

    private fun Float.progress(width: Float): Float =
        Math.min(1f, this / Math.min(width, 128f.toPx()))

    private fun Int.toPx(): Int = (density * this).toInt()
    private fun Float.toPx(): Float = density * this

    companion object {
        private const val BITMAP_SCALE = 0.5f
        private const val PARTICLE_MIN_RADIUS = 1f
        private const val PARTICLE_MAX_RADIUS = 2f
    }
}