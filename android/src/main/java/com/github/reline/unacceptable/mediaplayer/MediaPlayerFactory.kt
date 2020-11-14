package com.github.reline.unacceptable.mediaplayer

import android.media.MediaPlayer

interface MediaPlayerFactory {
    fun getMediaPlayer(): MediaPlayer
}
