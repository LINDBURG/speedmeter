package com.linbug.speedmeter

import android.content.Context
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import androidx.constraintlayout.widget.ConstraintLayout
import com.linbug.speedmeter.databinding.ViewSpeedBinding
import java.text.Format

class SpeedView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : ConstraintLayout(context, attrs, defStyle) {
    private var binding: ViewSpeedBinding
    private var oldDegree = 45f


    init {
        val layoutInflater = LayoutInflater.from(context)
        binding = ViewSpeedBinding.inflate(layoutInflater, this, true)
    }

    fun onSpeedChange( speed: Float) {
        binding.speed.text = resources.getString(R.string.speed_text, speed.toInt())
        val degrees = 45f + (speed % 180) * 1.5f

        val ra = RotateAnimation(oldDegree, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0f)
        ra.duration = 250
        ra.fillAfter = true
        binding.arrow.startAnimation(ra)
        oldDegree = degrees
    }

}