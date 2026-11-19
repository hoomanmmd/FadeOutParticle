package com.github.hoomanmmd.fadeoutparticle.xml

import android.graphics.Bitmap
import android.graphics.Bitmap.createBitmap
import android.graphics.Canvas
import android.view.View
import android.view.ViewTreeObserver

internal fun View.toBitmapOrNull(scale : Float): Bitmap? {
    val bitmap: Bitmap = runCatching {
        createBitmap((width * scale).toInt(), (height * scale).toInt(), Bitmap.Config.ARGB_8888)
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
            val observer = viewTreeObserver
            if (observer.isAlive) {
                observer.removeOnPreDrawListener(this)
            }
            callback()
            return true
        }
    })
}