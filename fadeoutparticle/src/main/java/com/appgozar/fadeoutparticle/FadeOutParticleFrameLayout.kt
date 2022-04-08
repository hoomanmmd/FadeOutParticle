package com.appgozar.fadeoutparticle

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.FrameLayout

class FadeOutParticleFrameLayout : FrameLayout {
    private val updateListener = ValueAnimator.AnimatorUpdateListener { invalidate() }
    private val animator = ValueAnimator.ofFloat(0f, 1f).apply {
        interpolator = DecelerateInterpolator()
    }
    private var fadeOutDrawable : FadeOutDrawable

    var animationDuration = 6000L
        set(value) {
            field = value
            animator.duration = value
        }

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
    ) : super(context, attrs, defStyleAttr)

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int,
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        val density = context.resources.displayMetrics.density
        fadeOutDrawable = FadeOutDrawable(density)
        val padding = (8 * density).toInt()
        setPadding(padding, padding, padding, padding)
        prepareAnimator()
    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.save()

        super.dispatchDraw(canvas)

        val animationValue = animator.animatedValue as Float
        fadeOutDrawable.draw(canvas, animationValue)

        canvas.restore()
    }


    /**
     *  Start Fade out Animation
     *  if layout is laid out, it start animation immediately
     *  otherwise it waits for it.
     *  see also:
     *  [reset]
     *  [addAnimatorListener]
     *  [animationDuration]
     */
    fun startAnimation() {
        if (isLaidOut) {
            start()
        } else {
            doOnPreDraw { start() }
        }
    }

    /**
     * Reset animation to initial state
     */
    fun reset() {
        animator.cancel()
        animator.duration = 0
        animator.removeUpdateListener(updateListener)
        animator.reverse()
        prepareAnimator()
    }

    /**
     * Add animation listener
     */
    fun addAnimatorListener(listener: Animator.AnimatorListener) {
        animator.addListener(listener)
    }

    private fun prepareAnimator() {
        animator.addUpdateListener(updateListener)
        animator.duration = animationDuration
    }

    private fun start() {
        if (animator.isRunning || animator.animatedValue == 1f) {
            return
        }
        fadeOutDrawable.prepare(this)
        animator.start()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        fadeOutDrawable.onLayout(left, top, right, bottom)
        super.onLayout(changed, left, top, right, bottom)
    }

    override fun onDetachedFromWindow() {
        animator.cancel()
        super.onDetachedFromWindow()
    }
}