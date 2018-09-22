package com.github.reline.unacceptable

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.unacceptable_layout.*

class MainActivity : Activity() {

    private lateinit var shake: Animation
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unacceptable_layout)

        // set application to control media volume instead of ring volume
        volumeControlStream = AudioManager.STREAM_MUSIC

        shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.lemon_grab_unacceptable)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        shake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {
                unacceptableButton.setImageResource(R.drawable.lemongrab)
            }

            override fun onAnimationEnd(animation: Animation) {
                unacceptableButton.setImageResource(R.drawable.lemon)
                vibrator.cancel()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    fun onLemonClicked(view: View) {
        if (shake.hasStarted() && !shake.hasEnded()) {
            return
        }
        shake.restrictDuration(mediaPlayer.duration.toLong())
        unacceptableButton.startAnimation(shake)
        vibrator.vibrate(mediaPlayer.duration)
        mediaPlayer.start()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val timeRemaining = savedInstanceState?.getInt(TIME_REMAINING) ?: 0
        val isChangingConfigurations = savedInstanceState?.getBoolean(CHANGING_CONFIGURATIONS) ?: false
        if (timeRemaining > 0 && isChangingConfigurations) {
            shake.restrictDuration(timeRemaining.toLong())
            unacceptableButton.startAnimation(shake)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        if (mediaPlayer.isPlaying) {
            if (isChangingConfigurations) {
                val timeRemaining = mediaPlayer.duration - mediaPlayer.currentPosition
                outState?.putInt(TIME_REMAINING, timeRemaining)
                outState?.putBoolean(CHANGING_CONFIGURATIONS, true)
            } else {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
                shake.cancel()
                unacceptableButton.animation = null
            }
        }
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val TIME_REMAINING = "TIME_REMAINING"
        private const val CHANGING_CONFIGURATIONS = "CHANGING_CONFIGURATIONS"
    }

}
