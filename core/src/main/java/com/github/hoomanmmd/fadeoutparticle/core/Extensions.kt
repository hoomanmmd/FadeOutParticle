package com.github.hoomanmmd.fadeoutparticle.core

import android.graphics.Bitmap
import android.graphics.Color
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt
import kotlin.random.Random

fun Int.withCoefficientOpacity(alphaCoefficient: Float) : Int{
    val currentOpacity = (this.shr(24).and(0xFF) * alphaCoefficient).toInt()
    return this.and(0x00FFFFFF).or(currentOpacity.shl(24))
}

fun Int.isTransparent() : Boolean = this.ushr(24) == 0

fun Float.interpolateYAxis(type: Int): Float {
    return when (type) {
        0 -> (this * this * ((1.7f + 1f) * this - 1.7f))
        1 -> -2.0.pow(-10.0 * this).toFloat() + 1
        2 -> sqrt(1.0 - (this - 1) * (this - 1)).toFloat()
        3 -> 1f - cos(this * Math.PI / 2.0).toFloat()
        4 -> this.toDouble().pow(3.0).toFloat()
        else -> 2.0.pow(10.0 * (this - 1)).toFloat()
    }
}

fun Bitmap.generateParticles(
    width: Float,
    height: Float,
    density: Float,
    scale : Float,
): List<Particle> {
    val space = 3
    val spacePx = density * space
    val verticalCount = max(1, (height / spacePx).toInt())
    val horizontalCount = max(1, (width / spacePx).toInt())

    val particles = ArrayList<Particle>((horizontalCount * verticalCount) / 4)

    var verticalOffset = spacePx / 2f
    repeat (verticalCount) {
        var horizontalOffset = 0f
        repeat(horizontalCount) {
            val radiusDp = Random.nextDouble(
                Constants.PARTICLE_MIN_RADIUS.toDouble(),
                Constants.PARTICLE_MAX_RADIUS.toDouble(),
            ).toFloat()

            val color = getPixel(
                (horizontalOffset * scale).toInt(),
                (verticalOffset * scale).toInt(),
                space,
            ).withCoefficientOpacity(radiusDp / Constants.PARTICLE_MAX_RADIUS)
            if(!color.isTransparent()) {
                val particle = Particle(
                    cx = horizontalOffset,
                    cy = verticalOffset,
                    radius = radiusDp * density,
                    color = color,
                    pathType = Random.nextInt(0, 5),
                    xFactor = (1 + Constants.PARTICLE_MAX_RADIUS - radiusDp) / Constants.PARTICLE_MAX_RADIUS,
                    yFactor = ((horizontalOffset + verticalOffset * 3) % 16) - 8,
                )
                particles.add(particle)
            }

            horizontalOffset += spacePx
        }
        verticalOffset += spacePx
    }

    return particles
}

private fun Bitmap.getPixel(x: Int, y: Int, space: Int): Int {
    val effectiveX = Math.max(0, x - space)
    val color = getPixel(effectiveX, y)
    if (effectiveX != 0 && space != 0 && color == Color.TRANSPARENT) {
        return getPixel(x, y, space - 1)
    }
    return color
}