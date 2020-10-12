package com.github.reline.unacceptable.appwidget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.os.Vibrator
import android.widget.RemoteViews
import com.github.reline.unacceptable.R
import com.github.reline.unacceptable.vibrate
import android.view.WindowManager
import android.graphics.BitmapFactory
import android.graphics.Matrix
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.concurrent.TimeUnit
import kotlin.math.min
import kotlin.math.sqrt

class AppWidgetController(
        context: Context,
        private val appWidgetManager: AppWidgetManager,
        private val appWidgetId: Int,
        private val mediaPlayer: MediaPlayer, // FIXME: release media player before finalizing
        private val vibrator: Vibrator,
        private val windowManager: WindowManager,
        private val coroutineScope: CoroutineScope
) {

    private val resources = context.resources
    private val lemongrab = BitmapFactory.decodeResource(resources, R.drawable.lemongrab, BitmapFactory.Options())
    private val packageName = context.packageName
    private val duration = mediaPlayer.duration

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
        coroutineScope.launch {
            animate()
        }
        vibrator.vibrate(duration)
    }

    private suspend fun animate() {
        while (mediaPlayer.isPlaying) {
            update()
//            delay(UPDATE_DELAY_MS) // update as fast as possible! creating bitmap is slow
        }
    }

    private fun update() {
        val bitmap = createBitmap()
        val views = getViews()
        views.setImageViewBitmap(R.id.unacceptableButton, bitmap)
        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun createBitmap(): Bitmap {
        val matrix = Matrix()
        val bitmap = if (mediaPlayer.isPlaying) {
            matrix.postRotate(calculateDegrees())
            lemongrab
        } else {
            BitmapFactory.decodeResource(resources, R.drawable.lemon, BitmapFactory.Options())
        }
        val width = bitmap.width
        val height = bitmap.height
        val scale = min(MAX_DIMENSION / width, MAX_DIMENSION / height).toFloat()
        matrix.postScale(scale, scale)
        // fixme: takes ~40ms
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
    }

    private fun calculateDegrees(): Float {
        val mediaPosition = mediaPlayer.currentPosition
        val animationPosition = mediaPosition % ROTATION_DURATION
        val center = ROTATION_DURATION / 2
        val side = if (animationPosition / center == 0L) {
            // positive/right
            1
        } else {
            // negative/left
            -1
        }
        val rotationPercentage = animationPosition.toFloat() % center.toFloat() / center.toFloat()
        val rotationDegrees = ROTATION_DELTA * 2 * rotationPercentage

        val direction = rotationDegrees / ROTATION_DELTA
        val degrees = if (direction <= 1) {
            rotationDegrees
        } else {
            ROTATION_DELTA * 2 - rotationDegrees
        }
        return degrees * side
    }

    private fun onCompletion() {
        update() // fixme: race condition
//        mediaPlayer.release() // fixme
    }

    private fun getViews(): RemoteViews {
        return RemoteViews(packageName, R.layout.unacceptable_layout)
    }

    fun onSizeChanged(maxWidth: Int, maxHeight: Int) {
        Timber.d("widget width = $maxWidth, widget height: $maxHeight")
    }

    companion object {
        private const val TAG = "AppWidgetController"

        //        private const val BYTES_PER_PIXEL = 4
        private const val MAX_PIXELS = 576000 // FIXME: can we determine this based off of metrics or just get a max size from onSizeChanged()?

        //        private const val MAX_SIZE = MAX_PIXELS * BYTES_PER_PIXEL
        private val MAX_DIMENSION = sqrt(MAX_PIXELS.toDouble()) // FIXME

        /**
         * Time it takes in ms for one full rotation; right delta, to left delta, back to center
         */
        private const val ROTATION_DURATION = 200L
        private const val ROTATION_DELTA = 15 // degrees allowed to travel

        private const val FPS = 30
        private val MILLIS_PER_SECOND = TimeUnit.SECONDS.toMillis(1)
        private val UPDATE_DELAY_MS = MILLIS_PER_SECOND / FPS
    }
}
