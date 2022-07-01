package com.appgozar.fadeoutparticle

import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewTreeObserver

internal fun View.toBitmapOrNull(scale : Float): Bitmap? {
    val bitmap: Bitmap = runCatching {
        Bitmap.createBitmap((width * scale).toInt(), (height * scale).toInt(), Bitmap.Config.ARGB_8888)
    }.getOrNull() ?: return null
    val canvas = Canvas(bitmap)
    canvas.scale(scale, scale)
    canvas.translate(-scrollX.toFloat(), -scrollY.toFloat())
    draw(canvas)
    canvas.setBitmap(null)
    return bitmap
}

internal fun View.doOnPreDraw(callback: () -> Unit){
    viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            viewTreeObserver.removeOnPreDrawListener(this)
            callback()
            return true
        }
    })
}
internal fun Int.withCoefficientOpacity(alphaCoefficient: Float) : Int{
    val currentOpacity = (this.shr(24).and(0xFF) * alphaCoefficient).toInt()
    return this.and(0x00FFFFFF).or(currentOpacity.shl(24))
}
internal fun Int.isTransparent() : Boolean = this.shr(24) == 0