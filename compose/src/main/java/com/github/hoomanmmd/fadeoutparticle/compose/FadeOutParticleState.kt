package com.github.hoomanmmd.fadeoutparticle.compose

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationConstants.DefaultDurationMillis
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.github.hoomanmmd.fadeoutparticle.core.Particle

/**
 * Holds the animation state. Create it with [rememberFadeOutParticleState].
 *
 * - [start]  : snapshots the content, builds the particles and plays the animation (suspends until done)
 * - [reset]  : brings the content back to its original, un-faded state
 */
@Stable
class FadeOutParticleState {
    internal val progress = Animatable(0f)
    internal var particles by mutableStateOf<List<Particle>>(emptyList())
    internal var animationRequest by mutableStateOf<AnimationRequest?>(null)
        private set

    val animationValue: Float
        get() = progress.value

    val isRunning: Boolean
        get() = progress.isRunning

    fun start(
        durationMillis: Int = 6000,
        easing: Easing = DecelerateEasing,
    ) {
        if (isRunning) return

        animationRequest = AnimationRequest(tween(durationMillis, easing = easing))
    }

    fun reset() {
        animationRequest = null
    }
}

@Composable
fun rememberFadeOutParticleState(): FadeOutParticleState =
    remember { FadeOutParticleState() }

internal class AnimationRequest(val spec: AnimationSpec<Float>)

private val DecelerateEasing = Easing { 1f - (1f - it) * (1f - it) }