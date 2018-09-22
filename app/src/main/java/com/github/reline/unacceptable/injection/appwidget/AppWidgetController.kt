package com.github.reline.unacceptable.injection.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.media.MediaPlayer
import android.os.Vibrator
import android.widget.RemoteViews
import com.github.reline.unacceptable.R
import com.github.reline.unacceptable.vibrate

class AppWidgetController(
        context: Context,
        private val appWidgetManager: AppWidgetManager,
        private val appWidgetId: Int,
        private val mediaPlayer: MediaPlayer,
        private val vibrator: Vibrator
) {

    private val packageName = context.packageName

    init {
        mediaPlayer.setOnCompletionListener {
            onCompletion()
        }
    }

    fun onClick() {
        if (mediaPlayer.isPlaying) {
            return
        }

        mediaPlayer.start()
        startAnimation()
        vibrator.vibrate(mediaPlayer.duration)
    }

    private fun startAnimation() {
        val views = getViews()
        views.setImageViewResource(R.id.unacceptableButton, R.drawable.lemongrab)
        appWidgetManager.updateAppWidget(appWidgetId, views)

        // TODO: animation
    }

    private fun onCompletion() {
        val views = getViews()
        views.setImageViewResource(R.id.unacceptableButton, R.drawable.lemon)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun getViews(): RemoteViews {
        return RemoteViews(packageName, R.layout.unacceptable_layout)
    }

}
