package com.github.hoomanmmd.fadeoutparticle.example

import android.content.res.ColorStateList
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.hoomanmmd.fadeoutparticle.compose.rememberFadeOutParticleState
import com.github.hoomanmmd.fadeoutparticle.xml.FadeOutParticleFrameLayout
import com.google.android.material.button.MaterialButton
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.github.hoomanmmd.fadeoutparticle.compose.fadeOutParticle

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView()
        setCompose()
    }

    private fun setCompose() {
        setContent {
            MaterialTheme() {
                FadeOutParticleCompose()
            }
        }
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

@Composable
private fun FadeOutParticleCompose() {
    val textState = rememberFadeOutParticleState()
    val iconState = rememberFadeOutParticleState()

    Column(
        Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_android),
                tint = Color((0xFFa0cf48).toInt()),
                modifier = Modifier.fadeOutParticle(iconState)
                    .width(48.dp)
                    .height(48.dp),
                contentDescription = ""
            )
            Spacer(Modifier.width(8.dp))
            Text("Fade out Particle", Modifier.fadeOutParticle(textState))

        }

        Button(
            onClick = {
                iconState.start(durationMillis = 4000)
                textState.start()
            },
            ) {
            Text("Start")
        }
        Button(onClick = {
            iconState.reset()
            textState.reset()
        }) {
            Text("Reset")
        }
    }
}