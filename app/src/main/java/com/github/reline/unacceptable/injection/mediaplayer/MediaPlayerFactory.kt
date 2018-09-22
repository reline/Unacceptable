package com.github.reline.unacceptable.injection.mediaplayer

import android.media.MediaPlayer

interface MediaPlayerFactory {
    fun getMediaPlayer(): MediaPlayer
}
