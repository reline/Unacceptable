package com.github.reline.unacceptable

import android.app.Activity
import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    private lateinit var shake: Animation
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var vibrator: Vibrator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // set application to control media volume instead of ring volume
        volumeControlStream = AudioManager.STREAM_MUSIC

        shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
        mediaPlayer = MediaPlayer.create(applicationContext, R.raw.lemon_grab_unacceptable)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

        shake.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

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
        mediaPlayer.start()
        unacceptableButton.startAnimation(shake)
        unacceptableButton.setImageResource(R.drawable.lemongrab)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            vibrator.vibrate(VibrationEffect.createOneShot(mediaPlayer.duration.toLong(), VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(mediaPlayer.duration.toLong())
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        super.onRestoreInstanceState(savedInstanceState)
        val timeRemaining = savedInstanceState?.getInt(TIME_REMAINING)?.toLong() ?: 0
        if (timeRemaining > 0) {
            unacceptableButton.setImageResource(R.drawable.lemongrab)
            shake.restrictDuration(timeRemaining)
            unacceptableButton.startAnimation(shake)
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        val timeRemaining = if (mediaPlayer.isPlaying) mediaPlayer.duration - mediaPlayer.currentPosition else 0
        outState?.putInt(TIME_REMAINING, timeRemaining)
        super.onSaveInstanceState(outState)
    }

    companion object {
        private const val TIME_REMAINING = "TIME_REMAINING"
    }

}
