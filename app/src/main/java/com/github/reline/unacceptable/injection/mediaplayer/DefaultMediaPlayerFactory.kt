package com.github.reline.unacceptable.injection.mediaplayer

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.github.reline.unacceptable.R

class DefaultMediaPlayerFactory(application: Application) : MediaPlayerFactory {

    private val context: Context = application

    override fun getMediaPlayer(): MediaPlayer {
        return MediaPlayer.create(context, R.raw.lemon_grab_unacceptable)
    }
}