package com.github.reline.unacceptable.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.os.Vibrator
import android.util.SparseArray
import android.view.WindowManager
import com.github.reline.unacceptable.mediaplayer.MediaPlayerFactory
import com.github.reline.unacceptable.getOrPut
import kotlinx.coroutines.GlobalScope

class DefaultAppWidgetControllerFactory(
        private val context: Context,
        private val mediaPlayerFactory: MediaPlayerFactory,
        private val vibrator: Vibrator,
        private val windowManager: WindowManager
) : AppWidgetControllerFactory {

    private val controllers = SparseArray<AppWidgetController>()

    override fun getAppWidgetController(appWidgetId: Int): AppWidgetController {
        return controllers.getOrPut(appWidgetId,
                AppWidgetController(
                        context,
                        AppWidgetManager.getInstance(context),
                        appWidgetId,
                        mediaPlayerFactory.getMediaPlayer(),
                        vibrator,
                        windowManager,
						GlobalScope
                )
        )
    }
}
