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
import timber.log.Timber
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.schedule

class AppWidgetController(
        context: Context,
        private val appWidgetManager: AppWidgetManager,
        private val appWidgetId: Int,
        private val mediaPlayer: MediaPlayer, // FIXME: release media player before finalizing
        private val vibrator: Vibrator,
        private val windowManager: WindowManager
) {

    private val resources = context.resources
    private val packageName = context.packageName
    private val duration = mediaPlayer.duration

    private var currentDirection = Direction.RIGHT
    private var currentDegrees = 0.0
    private var isRunning = false
    private var startTime = 0L

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
        vibrator.vibrate(duration)
    }

    private fun startAnimation() {
        Timber.d("FPS = $FPS, rotate duration = $ROTATE_DURATION, frames = $FRAMES, rotation per frame = $ROTATION_DEGREES")

        isRunning = true
        startTime = System.currentTimeMillis()
        update()
    }

    private fun update() {
        Timber.d("update()")
        Timber.d("isRunning = $isRunning")
        val resId = if (isRunning) R.drawable.lemongrab else R.drawable.lemon

        val options = BitmapFactory.Options()
        val bitmap = BitmapFactory.decodeResource(resources, resId, options)

        val width = bitmap.width
        val height = bitmap.height
        val matrix = Matrix()

        if (isRunning) {
            // rotate the bitmap based on the current degree of rotation and direction
            // frames / second * seconds passed = current frame
            val timePassed = System.currentTimeMillis() - startTime
            val currentFrame = timePassed / (MILLIS_PER_SECOND / FPS)
            val frameRotation = currentFrame * ROTATION_DEGREES
            val currentCycleRotation = frameRotation % MAX_DEGREES // absolute value of rotated degrees

            // default rotation direction is right, so use the min degrees
            val offset = currentCycleRotation - MIN_DEGREES // negative if right, positive if left
            Timber.d("time passed = $timePassed, current frame = $currentFrame, frame rotation = $frameRotation, current cycle rotation = $currentCycleRotation, offset = $offset")
            // determine direction based on total degrees rotated
            if (offset > 0) {
                // rotate left
                currentDirection = Direction.LEFT
            } else if (offset < 0) {
                // rotate right
                currentDirection = Direction.RIGHT
            }

            Timber.d("currentDirection = $currentDirection")
            Timber.d("currentDegrees = $currentDegrees")
            val degreesToRotate = Math.abs(offset)
            when (currentDirection) {
                Direction.RIGHT -> {
                    val newDegrees = currentDegrees + degreesToRotate
                    if (newDegrees > MAX_DEGREES) {
                        currentDirection = Direction.LEFT
                        currentDegrees += offset
                    } else {
                        currentDegrees -= Math.abs(offset)
                    }
                }
                Direction.LEFT -> {
                    val newDegrees = currentDegrees - degreesToRotate
                    if (newDegrees > MIN_DEGREES) {
                        currentDirection = Direction.RIGHT
                        currentDegrees -= degreesToRotate
                    } else {
                        currentDegrees += Math.abs(offset)
                    }
                }
            }
            Timber.d("newDegrees = $currentDegrees")
            matrix.postRotate(currentDegrees.toFloat())
        }

        // resize the bitmap to fit within the widget // TODO: dynamically based on app widget size
        val scale = Math.min(MAX_DIMENSION / width, MAX_DIMENSION / height).toFloat()
        matrix.postScale(scale, scale)

        val resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true)
        bitmap.recycle()

        // update the view with the rotated bitmap
        val views = getViews()
        views.setImageViewBitmap(R.id.unacceptableButton, resizedBitmap)
        appWidgetManager.updateAppWidget(appWidgetId, views)

        if (isRunning) {
            Timer().schedule(TimeUnit.SECONDS.toMillis(1) / FPS) {
                update()
            }
        }
    }

    private fun onCompletion() {
        isRunning = false
        currentDegrees = 0.0
        currentDirection = Direction.RIGHT
        update()
    }

    private fun getViews(): RemoteViews {
        return RemoteViews(packageName, R.layout.unacceptable_layout)
    }

    fun onSizeChanged(maxWidth: Int, maxHeight: Int) {
        Timber.d("widget width = $maxWidth, widget height: $maxHeight")
    }

    private enum class Direction {
        RIGHT,
        LEFT
    }

    companion object {
        private const val TAG = "AppWidgetController"
//        private const val BYTES_PER_PIXEL = 4
        private const val MAX_PIXELS = 576000 // FIXME: can we determine this based off of metrics or just get a max size from onSizeChanged()?
//        private const val MAX_SIZE = MAX_PIXELS * BYTES_PER_PIXEL
        private val MAX_DIMENSION = Math.sqrt(MAX_PIXELS.toDouble()) // FIXME
        private const val FPS = 30
        private const val ROTATE_DURATION = 100L // milliseconds
        private const val MIN_DEGREES = -15.0
        private const val MAX_DEGREES = 15.0

        private val DEGREES_TOTAL = Math.abs(MIN_DEGREES) + Math.abs(MAX_DEGREES)
        // frames = fps / (milliseconds in a second / rotate duration)
        private const val FRAMES = FPS * (ROTATE_DURATION / 1000.00) // 30 * .1 = 3
        // rotation degrees = degrees / frames
        private val ROTATION_DEGREES = FRAMES / DEGREES_TOTAL
        private const val MILLIS_PER_SECOND = 1000
    }

    // start time
    // time since started in millis / 1000 * 30fps = draw frame (this will enable us to skip frames)



    // fps * duration = frames
    // rotate(degrees / frame)
//    private val duration = mediaPlayer.duration
//    private val frames = FPS * TimeUnit.MILLISECONDS.toSeconds(duration.toLong())
//    private val rotation = DEGREES_TOTAL / frames

    // 30fps * 3s = 90frames
    // abs(-15) + abs(15) = 30 degrees total
    // 30 degrees / 90 frames = .33 degrees per frame

    // should take 100 milliseconds from 0 to 5 degrees
    // 30 fps is 30 frames per 1000 milliseconds
    // 30 / 10 = 3 frames for 5 degrees
    // 5 / 3 = 1.66 degrees per frame


}
