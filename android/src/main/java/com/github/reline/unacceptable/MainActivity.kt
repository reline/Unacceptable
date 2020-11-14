package com.github.reline.unacceptable

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Vibrator
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.github.reline.unacceptable.mediaplayer.MediaPlayerFactory
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.unacceptable_layout.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var mediaPlayerFactory: MediaPlayerFactory

    @Inject
    lateinit var vibrator: Vibrator

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var shake: Animation

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.unacceptable_layout)

        // set application to control media volume instead of ring volume
        volumeControlStream = AudioManager.STREAM_MUSIC

        mediaPlayer = mediaPlayerFactory.getMediaPlayer()
        shake = AnimationUtils.loadAnimation(applicationContext, R.anim.shake)
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

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val timeRemaining = savedInstanceState.timeRemaining
        if (timeRemaining > 0 && savedInstanceState.isChangingConfigurations) {
            shake.restrictDuration(timeRemaining.toLong())
            unacceptableButton.startAnimation(shake)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        if (mediaPlayer.isPlaying) {
            if (isChangingConfigurations) {
				outState.timeRemaining = mediaPlayer.duration - mediaPlayer.currentPosition
				outState.isChangingConfigurations = true
            } else {
                mediaPlayer.pause()
                mediaPlayer.seekTo(0)
                shake.cancel()
                unacceptableButton.animation = null
            }
        }
        super.onSaveInstanceState(outState)
    }
}

private const val TIME_REMAINING = "TIME_REMAINING"
private var Bundle.timeRemaining
	get() = getInt(TIME_REMAINING)
	set(value) = putInt(TIME_REMAINING, value)

private const val CHANGING_CONFIGURATIONS = "CHANGING_CONFIGURATIONS"
private var Bundle.isChangingConfigurations
	get() = getBoolean(CHANGING_CONFIGURATIONS)
	set(value) = putBoolean(CHANGING_CONFIGURATIONS, value)
