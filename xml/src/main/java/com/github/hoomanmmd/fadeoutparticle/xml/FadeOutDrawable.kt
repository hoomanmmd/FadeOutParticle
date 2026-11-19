package com.github.hoomanmmd.fadeoutparticle.xml

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.LinearGradient
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.RectF
import android.graphics.Shader
import android.view.View
import com.github.hoomanmmd.fadeoutparticle.core.Constants
import com.github.hoomanmmd.fadeoutparticle.core.Particle
import com.github.hoomanmmd.fadeoutparticle.core.generateParticles
import com.github.hoomanmmd.fadeoutparticle.core.interpolateYAxis

internal class FadeOutDrawable(var density: Float) {
    private val rectF = RectF()
    private var particles: List<Particle>? = null
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
        if (animationValue == 0f) {
            return
        }

        val width = rectF.width()
        val limitedWidth = minOf(width, Constants.LIMIT_WIDTH_DP.toPx())
        val targetWidth = width * (1 - animationValue * (1 + limitedWidth / width))
        fadeMatrix.reset()
        fadeMatrix.postTranslate(targetWidth, 0f)
        fadePaint.shader.setLocalMatrix(fadeMatrix)
        canvas.drawRect(rectF, fadePaint)
        particles?.run { drawParticles(canvas, targetWidth, limitedWidth) }
    }

    private fun drawParticles(canvas: Canvas, limit: Float, limitedWidth: Float) {
        val particles = particles ?: return
        val xCoefficient = Constants.X_COEFFICIENT.toPx()
        val yCoefficient = Constants.Y_COEFFICIENT.toPx()
        for (particle in particles) {
            if (particle.cx <= limit) {
                continue
            }
            val particleProgress  = ((particle.cx - limit) / limitedWidth).coerceAtMost(1f)

            particlePaint.color = particle.color
            particlePaint.alpha = (particlePaint.alpha * (1f - particleProgress)).toInt()
            canvas.drawCircle(
                particle.cx + particleProgress * xCoefficient * particle.xFactor,
                particle.cy + particleProgress.interpolateYAxis(particle.pathType) * yCoefficient * particle.yFactor,
                particle.radius,
                particlePaint,
            )

        }
    }

    fun prepare(child: View?) {
        child ?: return
        density = child.resources.displayMetrics.density
        child.toBitmapOrNull(Constants.BITMAP_SCALE)?.also { bitmap ->
            particles = bitmap.generateParticles(width = rectF.width(), height = rectF.height(), density = density, scale = Constants.BITMAP_SCALE)
            bitmap.recycle()
        }
    }

    private fun Float.toPx(): Float = density * this
}