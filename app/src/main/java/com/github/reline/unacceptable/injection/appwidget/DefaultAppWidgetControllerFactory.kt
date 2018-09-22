package com.github.reline.unacceptable.injection.appwidget

import android.app.Application
import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Vibrator
import android.util.SparseArray
import com.github.reline.unacceptable.injection.mediaplayer.MediaPlayerFactory
import com.github.reline.unacceptable.getOrPut

class DefaultAppWidgetControllerFactory(
        application: Application,
        private val mediaPlayerFactory: MediaPlayerFactory,
        private val vibrator: Vibrator
) : AppWidgetControllerFactory {

    private val context: Context = application
    private val controllers = SparseArray<AppWidgetController>()

    override fun getAppWidgetController(appWidgetId: Int): AppWidgetController {
        return controllers.getOrPut(appWidgetId,
                AppWidgetController(
                        context,
                        AppWidgetManager.getInstance(context),
                        appWidgetId,
                        mediaPlayerFactory.getMediaPlayer(),
                        vibrator
                )
        )
    }
}