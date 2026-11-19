package com.github.hoomanmmd.fadeoutparticle.compose

import android.graphics.Bitmap
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.layer.GraphicsLayer
import androidx.compose.ui.graphics.layer.drawLayer
import androidx.compose.ui.graphics.rememberGraphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import com.github.hoomanmmd.fadeoutparticle.core.Constants
import com.github.hoomanmmd.fadeoutparticle.core.Particle
import com.github.hoomanmmd.fadeoutparticle.core.generateParticles
import com.github.hoomanmmd.fadeoutparticle.core.interpolateYAxis
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.withContext


/**
 * Applies the fade-out particle effect to composable:
 *
 *     Text("Hello", Modifier.fadeOutParticle(state))
 */
@Composable
fun Modifier.fadeOutParticle(
    state: FadeOutParticleState,
    applyPadding : Boolean = true,
    ): Modifier {
    val layer = rememberGraphicsLayer()
    val density by rememberUpdatedState(LocalDensity.current)

    LaunchedEffect(state, layer) {
        snapshotFlow { state.animationRequest }
            .distinctUntilChanged()
            .collectLatest { animationRequest ->
                if (animationRequest != null) {
                    state.play(layer, animationRequest.spec, { density })
                } else {
                    state.restore()
                }
            }
    }

    val modifier =  this
        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
        .drawWithContent {
            layer.record { this@drawWithContent.drawContent() }
            drawLayer(layer)

            val progress = state.progress.value
            if (progress == 0f) {
                return@drawWithContent
            }

            val width = size.width
            val limited = minOf(width, Constants.LIMIT_WIDTH_DP.dp.toPx())
            val edge = width - progress * (width + limited)

            drawRect(
                brush = Brush.horizontalGradient(
                    0f to Color.White,
                    1f to Color.Transparent,
                    startX = edge,
                    endX = edge + 8.dp.toPx(),
                ),
                blendMode = BlendMode.DstIn,
            )

            drawParticles(state.particles, edge, limited)
        }

    if(!applyPadding){
        return modifier
    }

    return modifier.padding(8.dp)
}

private fun DrawScope.drawParticles(
    particles: List<Particle>,
    limit: Float,
    limitedWidth: Float,
) {
    if (particles.isEmpty()) {
        return
    }

    val xCoefficient = Constants.X_COEFFICIENT.dp.toPx()
    val yCoefficient = Constants.Y_COEFFICIENT.dp.toPx()

    for (particle in particles) {
        if (particle.cx <= limit) {
            continue
        }
        val progress = ((particle.cx - limit) / limitedWidth).coerceAtMost(1f)

        drawCircle(
            color = Color(particle.color),
            radius = particle.radius,
            alpha = 1f - progress,
            center = Offset(
                x = particle.cx + progress * xCoefficient * particle.xFactor,
                y = particle.cy + progress.interpolateYAxis(particle.pathType) * yCoefficient * particle.yFactor,
            ),
        )
    }
}


private suspend fun FadeOutParticleState.play(layer: GraphicsLayer, spec: AnimationSpec<Float>, density: () -> Density) {
    while (layer.size.width <= 0 || layer.size.height <= 0) {
        withFrameNanos { }
    }

    val androidBitmap = layer.toImageBitmap().asAndroidBitmap()
    val copied = androidBitmap.config == Bitmap.Config.HARDWARE
    val bitmap = if (copied) androidBitmap.copy(Bitmap.Config.ARGB_8888, false) else androidBitmap
    val width = layer.size.width
    val height = layer.size.height
    val density = density().density

    particles = withContext(Dispatchers.Default) {
        bitmap.generateParticles(width.toFloat(), height.toFloat(), density, 1f)
    }

    if (copied) {
        bitmap.recycle()
    }

    progress.animateTo(1f, spec)
}

private suspend fun FadeOutParticleState.restore() {
    progress.snapTo(0f)
    particles = emptyList()
}