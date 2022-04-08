package com.appgozar.example

import android.app.Activity
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.appgozar.fadeoutparticle.FadeOutParticleFrameLayout
import com.google.android.material.button.MaterialButton

class MainActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView()
    }

    private fun setContentView() {
        val context = this
        val container = FrameLayout(context).apply {
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.MATCH_PARENT,
            )
        }
        val textParticle = FadeOutParticleFrameLayout(context).apply {
            TextView(context).apply {
                text = "Fade out Particle"
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                )
                addView(this)
            }
            animationDuration = 4000
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                gravity = Gravity.CENTER_VERTICAL
            }
        }
        val iconParticle = FadeOutParticleFrameLayout(context).apply {
            ImageView(context).apply {
                setImageResource(R.drawable.ic_android)
                imageTintList = ColorStateList.valueOf((0xFFa0cf48).toInt())
                layoutParams = FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                    FrameLayout.LayoutParams.WRAP_CONTENT,
                )
                addView(this)
            }
            animationDuration = 4000
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
            )
        }
        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            addView(iconParticle)
            addView(textParticle)
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                gravity = Gravity.CENTER
            }
            container.addView(this)
        }
        LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT,
            ).apply {
                gravity = Gravity.BOTTOM
            }
            MaterialButton(context).apply {
                text = "Start"
                setOnClickListener {
                    textParticle.startAnimation()
                    iconParticle.startAnimation()
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f,
                )
                addView(this)
            }

            MaterialButton(context).apply {
                text = "Reset"
                setOnClickListener {
                    textParticle.reset()
                    iconParticle.reset()
                }
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f,
                )
                addView(this)
            }
            container.addView(this)
        }

        setContentView(container)
    }

    private fun setContentViewFromXml() {
        setContentView(R.layout.main_activity)
        val fadeOutParticleView = findViewById<FadeOutParticleFrameLayout>(R.id.fade_out_particle)
        val button = findViewById<View>(R.id.button)
        button.setOnClickListener { fadeOutParticleView.startAnimation() }
    }
}