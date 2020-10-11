package com.github.reline.unacceptable.mediaplayer

import android.content.Context
import android.media.MediaPlayer
import com.github.reline.unacceptable.R

class DefaultMediaPlayerFactory(private val context: Context) : MediaPlayerFactory {

    override fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer.create(context, R.raw.lemon_grab_unacceptable)
    }
}
